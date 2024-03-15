package ru.trubino.farm.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Пользователи",
        description = "Позволяет Владельцу регистрировать новых пользователей," +
                " удалять пользователей (перманентно или времмено)," +
                " получать информацию о пользователях," +
                " редактировать информацию о пользователях."
)
@RestController
@PreAuthorize("hasRole('OWNER')")
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(summary = "Создает нового пользователя в системе",
            description = "Создает нового пользователя в системе на основе ФИО, пароля и электронной почты")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto){
        return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(userDto));
    }

    @Operation(summary = "Возвращает информацию о всех пользователях",
            description = "Возвращает список с инфомацией о пользователях")
    @GetMapping("")
    public ResponseEntity<?> findAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAllUsers());
    }

    @Operation(summary = "Возвращает информацию о пользователе",
            description = "Возвращает информацию о пользователе с указанным идентификационным номером")
    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserById(id));
    }

    @Operation(summary = "Перманентно удаляет пользователя",
            description = "Перманентно удаляет с указанным идентификационным номером")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Обновляет информацию о пользователе",
            description = "Обновляет информацию о пользователе на основе индентификационного номера." +
                    " Позволяет обновить ФИО, пароль и электронную почту." +
                    " Также, позволяет временно удалить пользователя установив параметр enabled=false"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserById(
            @PathVariable Long id,
            @RequestBody(required=false) UserDto userDto,
            @RequestParam(defaultValue = "true", required=false) Boolean accountNonExpired,
            @RequestParam(defaultValue = "true", required=false) Boolean credentialsNonExpired,
            @RequestParam(defaultValue = "true", required=false) Boolean accountNonLocked,
            @RequestParam(defaultValue = "true", required=false) Boolean enabled
    ){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserById(
                id,
                userDto,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                enabled
        ));
    }

}
