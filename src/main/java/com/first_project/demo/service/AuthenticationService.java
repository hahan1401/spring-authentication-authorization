package com.first_project.demo.service;

import com.first_project.demo.Entity.InvalidatedToken;
import com.first_project.demo.Entity.User;
import com.first_project.demo.dto.request.AuthenticationRequest;
import com.first_project.demo.dto.request.IntrospectRequest;
import com.first_project.demo.dto.request.LogoutRequest;
import com.first_project.demo.dto.response.ApiResponse;
import com.first_project.demo.dto.response.AuthenticationResponse;
import com.first_project.demo.dto.response.IntrospectResponse;
import com.first_project.demo.exception.AppException;
import com.first_project.demo.exception.ErrorCode;
import com.first_project.demo.repository.InvalidatedTokenRepository;
import com.first_project.demo.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  @NonFinal
  protected static final String SIGNER_KEY = "ed3ywfmeCHi39mBwk76SNy+hJAMQA+9P1Irg8J+zV/hne3tfHytmC1G74lQWg4aF";
  private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
  private final UserRepository userRepository;
  private final InvalidatedTokenRepository invalidatedTokenRepository;

  public ApiResponse<AuthenticationResponse> authenticate(AuthenticationRequest request) {
    User user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
    boolean authenticated = user.getPassword().equals(request.getPassword());
    if (!authenticated) {
      return ApiResponse.<AuthenticationResponse>builder()
          .responseData(AuthenticationResponse.builder().authenticated(false).build()).build();
    }

    return ApiResponse.<AuthenticationResponse>builder().responseData(
            AuthenticationResponse.builder().authenticated(true).token(generateToken(user)).build())
        .build();
  }

  public String generateToken(User user) {
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
    JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().subject(user.getUsername())
        .issuer("spring-demo-app").issueTime(new Date())
        .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
        .jwtID(UUID.randomUUID().toString())
        .claim("userId", user.getId()).claim("scope", buildScope(user)).build();
    Payload payload = new Payload(jwtClaimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(header, payload);

    try {
      jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      log.error("Cannot create token");
      throw new RuntimeException(e);
    }
  }

  public void logout(LogoutRequest request) throws ParseException, JOSEException {
    SignedJWT signToken = verifyToken(request.getToken());

    String jit = signToken.getJWTClaimsSet().getJWTID();

    Date expiryDate = signToken.getJWTClaimsSet().getExpirationTime();

    InvalidatedToken invalidatedToken = InvalidatedToken.builder().id(jit).expiryTime(expiryDate)
        .build();

    invalidatedTokenRepository.save(invalidatedToken);
  }

  public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
    String token = request.getToken();
    boolean isValid = true;

    try {
      verifyToken(token);
    } catch (AppException exception) {
      isValid = false;
    }

    return IntrospectResponse.builder().valid(isValid).build();
  }

  private String buildScope(User user) {
    StringJoiner stringJoiner = new StringJoiner(" ");
    if (!CollectionUtils.isEmpty(user.getRoles())) {
      user.getRoles().forEach(role -> {
        stringJoiner.add("ROLE_" + role.getName());
        if (!CollectionUtils.isEmpty(role.getPermissions())) {
          role.getPermissions().forEach(permission -> {
            stringJoiner.add(permission.getName());
          });
        }
      });
    }
    return stringJoiner.toString();
  }

  private SignedJWT verifyToken(String token) {
    try {
      JWSVerifier verifier = new MACVerifier((SIGNER_KEY.getBytes()));
      try {
        SignedJWT signedJWT = SignedJWT.parse(token);
        boolean verified = signedJWT.verify(verifier);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean isValidToken =
            verified && expiryTime.after(new Date());

        if (!isValidToken) {
          throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
          throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }
}
