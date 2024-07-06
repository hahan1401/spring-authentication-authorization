package com.first_project.demo.controller;

import com.first_project.demo.dto.request.IntrospectRequest;
import com.first_project.demo.dto.request.LogoutRequest;
import com.first_project.demo.dto.response.ApiResponse;
import com.first_project.demo.dto.request.AuthenticationRequest;
import com.first_project.demo.dto.response.AuthenticationResponse;
import com.first_project.demo.dto.response.IntrospectResponse;
import com.first_project.demo.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        ApiResponse<AuthenticationResponse> result =  authenticationService.authenticate(request);
        return result;
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> isValidToken(@RequestBody IntrospectRequest request)
        throws ParseException, JOSEException {
        IntrospectResponse isValidToken = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().responseData(isValidToken).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }
}
