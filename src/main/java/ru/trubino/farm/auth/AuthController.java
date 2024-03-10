package ru.trubino.farm.auth;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Authentication Controller",
        description = "Allows you to register, login or logout a user. Also there's method that returns new access token if current refresh token isn't expired."
)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Operation(
            summary = "Logins user",
            description = "Allows you to login existing user based on its username and password. It adds 'refreshToken' cookie to the browser."
    )
    @PostMapping
    public ResponseEntity<?> openSession(HttpServletResponse response, @RequestBody AuthDto authDto){
        JwtDto jwtDto = authService.openSession(response, authDto);
        return ResponseEntity.status(HttpStatus.OK).body(jwtDto);
    }

    @Operation(
            summary = "Returns access token",
            description = "Allows you to get access token if 'refreshToken' cookie isn't expired."
    )
    @GetMapping
    public ResponseEntity<?> extendSession(HttpServletRequest request){
        JwtDto jwtDto = authService.extendSession(request);
        return ResponseEntity.status(HttpStatus.OK).body(jwtDto);
    }

    @Operation(
            summary = "Logouts user",
            description = "Removes 'refreshToken' cookie from the browser so user cannot get new access token."
    )
    @DeleteMapping
    public ResponseEntity<?> closeSession(HttpServletResponse response){
        authService.closeSession(response);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
