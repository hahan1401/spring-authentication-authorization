package com.first_project.demo.controller;

import com.first_project.demo.dto.request.IntrospectRequest;
import com.first_project.demo.dto.response.ApiResponse;
import com.first_project.demo.dto.request.AuthenticationRequest;
import com.first_project.demo.dto.response.AuthenticationResponse;
import com.first_project.demo.dto.response.IntrospectResponse;
import com.first_project.demo.service.AuthenticationService;
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
    ApiResponse<IntrospectResponse> isValidToken(@RequestBody IntrospectRequest request) {
        IntrospectResponse isValidToken = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().responseData(isValidToken).build();
    }
}
