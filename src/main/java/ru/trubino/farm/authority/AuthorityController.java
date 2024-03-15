package ru.trubino.farm.authority;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Привилегии",
        description = "Позволяет Владельцу выдать или отозвать привилегию пользователя." +
                " Также, позволяет Владельцу посмотреть все доступные для назначения привилегии." +
                " Позволяет Владельцу посмотреть список пользователей с заданной привилегией."

)
@RestController
@PreAuthorize("hasRole('OWNER')")
@RequestMapping("/api/v1/users")
public class AuthorityController {

    @Autowired
    AuthorityService authorityService;

    @Operation(
            summary = "Выдает привилегию",
            description = "Выдает привилегию пользователю на основе идентификационных номеров пользователя и привилегии"
    )
    @PostMapping("/{userId}/authorities/{authorityId}")
    public ResponseEntity<?> grantAuthorityToUser(@PathVariable Long userId, @PathVariable Long authorityId){
        return ResponseEntity.status(HttpStatus.OK).body(authorityService.grantAuthorityToUser(authorityId, userId));
    }

    @Operation(
            summary = "Отзывает привилегию",
            description = "Отзывает привилегию пользователя на основе идентификационных номеров пользователя и привилегии"
    )
    @DeleteMapping("/{userId}/authorities/{authorityId}")
    public ResponseEntity<?> revokeAuthorityFromUser(@PathVariable Long userId, @PathVariable Long authorityId){
        return ResponseEntity.status(HttpStatus.OK).body(authorityService.revokeAuthorityFromUser(authorityId, userId));
    }

    @Operation(
            summary = "Возвращает список всех првилегий",
            description = "Возвращает список всех првилегий"
    )
    @GetMapping("/authorities")
    public ResponseEntity<?> findAllAuthorities(){
        return ResponseEntity.status(HttpStatus.OK).body(authorityService.findAllAuthorities());
    }

    @Operation(
            summary = "Возвращает список пользователей с заданной привилегией",
            description = "Возвращает список пользователей с заданной привилегией"
    )
    @GetMapping("/authorities/{authorityId}")
    public ResponseEntity<?> findAllUsersByAuthorityId(@PathVariable Long authorityId){
        return ResponseEntity.status(HttpStatus.OK).body(authorityService.findUsersByAuthorityId(authorityId));
    }
}
