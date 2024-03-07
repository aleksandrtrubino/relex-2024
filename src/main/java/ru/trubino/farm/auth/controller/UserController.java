package ru.trubino.farm.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.trubino.farm.auth.model.User;
import ru.trubino.farm.auth.repository.UserRepository;

@Tag(
        name = "User Controller",
        description = "Lets you get information about users"
)
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Operation(
            summary = "Gets list of all users",
            description = "Returns list of all users as a JSON file or null if there's no one"
    )
    @GetMapping
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
    }

    @Operation(
            summary = "Gets user by id",
            description = "Returns a user based on its id as a JSON file or null if there's no one"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        User user = userRepository.findById(id).orElse(null);
        return user != null ? ResponseEntity.status(HttpStatus.OK).body(user) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
