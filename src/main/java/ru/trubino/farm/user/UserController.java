package ru.trubino.farm.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "User Controller",
        description = "Allows you to get information about users"
)
@RestController
@PreAuthorize("hasRole('OWNER')")
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(summary = "Registers user", description = "Allows you to register new user based on its username, password and email")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto){
        return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(userDto));
    }

    @Operation(summary = "Gets list of all users", description = "Returns list of all users as a JSON file or null if there's no one")
    @GetMapping("")
    public ResponseEntity<?> findAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAllUsers());
    }

    @Operation(summary = "Gets user by id", description = "Returns a user based on its id as a JSON file or null if there's no one")
    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserById(id));
    }

    @Operation(summary = "Deletes user", description = "Deletes user by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "",
            description = ""
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
