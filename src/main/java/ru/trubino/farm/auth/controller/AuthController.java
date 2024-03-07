package ru.trubino.farm.auth.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.trubino.farm.auth.service.AuthService;
import ru.trubino.farm.auth.payload.request.AssignRoleRequest;
import ru.trubino.farm.auth.payload.request.CreateRoleRequest;
import ru.trubino.farm.auth.payload.request.LoginRequest;
import ru.trubino.farm.auth.payload.request.RegisterRequest;
import ru.trubino.farm.auth.payload.response.AccessTokenResponse;

@Tag(
        name = "Authentication Controller",
        description = "Allows you to register, login or logout a user. Also there's method that returns new access token if current refresh token isn't expired."
)
@RestController
@RequestMapping("/api/v1/security")
public class AuthController {

    @Autowired
    AuthService authService;

    @Operation(
            summary = "Registers user",
            description = "Allows you to register new user based on its username, password and email"
    )
    @GetMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Logins user",
            description = "Allows you to login existing user based on its username and password. It adds \"refreshToken\" cookie to the browser."
    )
    @GetMapping("/login")
    public ResponseEntity<?> login(HttpServletResponse response, @RequestBody LoginRequest loginRequest){
        AccessTokenResponse tokenResponse = authService.login(response,loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
    }

    @Operation(
            summary = "Returns access token",
            description = "Allows you to get access token if \"refreshToken\" cookie isn't expired."
    )
    @GetMapping("/token")
    public ResponseEntity<?> getAccessToken(HttpServletRequest request){
        AccessTokenResponse tokenResponse = authService.getAccessToken(request);
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
    }

    @Operation(
            summary = "Logouts user",
            description = "Removes \"refreshToken\" cookie from the browser so user cannot get new access token."
    )
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){
        authService.logout(response);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Creates new role",
            description = "Creates new role"
    )
    @PostMapping("/roles")
    public ResponseEntity<?> createRole(@RequestBody CreateRoleRequest createRoleRequest){
        return ResponseEntity.status(HttpStatus.OK).body(authService.createRole(createRoleRequest));
    }

    @Operation(
            summary = "Assigns role",
            description = "Assigns role to the user"
    )
    @PutMapping("/roles/users")
    public ResponseEntity<?> assignRole(@RequestBody AssignRoleRequest assignRoleRequest){
        return ResponseEntity.status(HttpStatus.OK).body(authService.assignRole(assignRoleRequest));
    }
}
