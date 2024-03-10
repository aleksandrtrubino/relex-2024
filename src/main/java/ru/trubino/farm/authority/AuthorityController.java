package ru.trubino.farm.authority;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Authority Controller",
        description = ""

)
@RestController
@PreAuthorize("hasRole('OWNER')")
@RequestMapping("/api/v1/users")
public class AuthorityController {

    @Autowired
    AuthorityService authorityService;

    @Operation(
            summary = "",
            description = ""
    )
    @PostMapping("/{userId}/authorities/{authorityId}")
    public ResponseEntity<?> grantAuthorityToUser(@PathVariable Long userId, @PathVariable Long authorityId){
        return ResponseEntity.status(HttpStatus.OK).body(authorityService.grantAuthorityToUser(authorityId, userId));
    }

    @Operation(
            summary = "",
            description = ""
    )
    @DeleteMapping("/{userId}/authorities/{authorityId}")
    public ResponseEntity<?> revokeAuthorityFromUser(@PathVariable Long userId, @PathVariable Long authorityId){
        return ResponseEntity.status(HttpStatus.OK).body(authorityService.revokeAuthorityFromUser(authorityId, userId));
    }

    @Operation(
            summary = "",
            description = ""
    )
    @GetMapping("/authorities")
    public ResponseEntity<?> findAllAuthorities(){
        return ResponseEntity.status(HttpStatus.OK).body(authorityService.findAllAuthorities());
    }

    @Operation(
            summary = "",
            description = ""
    )
    @GetMapping("/authorities/{authorityId}")
    public ResponseEntity<?> findAllUsersByAuthorityId(@PathVariable Long authorityId){
        return ResponseEntity.status(HttpStatus.OK).body(authorityService.findUsersByAuthorityId(authorityId));
    }
}
