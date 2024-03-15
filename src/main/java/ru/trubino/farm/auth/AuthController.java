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
        name = "Аутентификация",
        description = "Аутентификация основана на JWT(JSON Web Tokens)." +
                " Когда пользователь входит в систему, на браузер устанавливается http-only куки 'refreshToken'," +
                " который является долгосрочным токеном. В это же время в виде JSON возвращается accessToken," +
                " который является короткоживущим токеном. Последний токен должен прикрепляться к каждому запросу пользователя" +
                " в виде заголовка 'Authorization'. Когда время жизни accessToken'а истекает, пользователь должен получить новый токен," +
                " который выдается на основе куки 'refreshToken'. Когда время жизни refreshToken'а истекает," +
                " пользователь должен заново войти в систему."
)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Operation(
            summary = "Позволяет пользователю войти в систему",
            description = "Позволяет пользователю войти в систему на основе электронной почты и пароля, устанавливает refreshToken-куки"
    )
    @PostMapping
    public ResponseEntity<?> openSession(HttpServletRequest request, HttpServletResponse response, @RequestBody AuthDto authDto){
        TokenDto tokenDto = authService.openSession(request, response, authDto);
        return ResponseEntity.status(HttpStatus.OK).body(tokenDto);
    }

    @Operation(
            summary = "Выдает новый accessToken",
            description = "Возвращает новый accessToken в виде JSON"
    )
    @GetMapping
    public ResponseEntity<?> extendSession(HttpServletRequest request){
        TokenDto tokenDto = authService.extendSession(request);
        return ResponseEntity.status(HttpStatus.OK).body(tokenDto);
    }

    @Operation(
            summary = "Позволяет пользователю выйти из системы",
            description = "Позволяет пользователю выйти из системы, удаляет refreshToken-куки из браузера"
    )
    @DeleteMapping
    public ResponseEntity<?> closeSession(HttpServletResponse response){
        authService.closeSession(response);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
