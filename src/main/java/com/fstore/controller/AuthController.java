package com.fstore.controller;

import com.fstore.exception.dto.ErrorDto;
import com.fstore.mapper.UserAuthMapper;
import com.fstore.model.UserAuth;
import com.fstore.model.dto.AuthResponseDto;
import com.fstore.model.dto.LoginRequestDto;
import com.fstore.model.dto.UserLoggedInResponseDto;
import com.fstore.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.fstore.model.dto.AuthResponseTypeDto.SUCCESSFUL_LOGOUT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final UserAuthMapper mapper;
    private final AuthService service;

    @Operation(summary = "Check who am I flow")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User is logged in and retrieved",
                    content = @Content(schema = @Schema(implementation = UserLoggedInResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("/whoami")
    public ResponseEntity<UserLoggedInResponseDto> checkWhoAmI(
            @RequestAttribute UserAuth auth) {
        UserLoggedInResponseDto loggedInResponse =
                mapper.toDto(service.checkWhoAmIFlow(auth.email()));

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, service.getJwtCookieFor(auth.email()))
                .body(loggedInResponse);
    }

    @Operation(summary = "Init login flow")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successfully finished",
                    content = @Content(schema = @Schema(implementation = UserLoggedInResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<UserLoggedInResponseDto> initLogin(
            @RequestBody LoginRequestDto loginRequest) {
        UserLoggedInResponseDto loggedInResponse = mapper.toDto(
                service.loginFlow(loginRequest.email(), loginRequest.password()));

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, service.getJwtCookieFor(loggedInResponse.email()))
                .body(loggedInResponse);
    }

    @Operation(summary = "Init logout flow")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successfully finished",
                    content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping("/logout")
    public ResponseEntity<AuthResponseDto> initLogout() {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, service.logoutFlow())
                .body(new AuthResponseDto(SUCCESSFUL_LOGOUT));
    }
}
