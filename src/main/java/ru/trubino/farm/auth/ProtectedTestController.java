package ru.trubino.farm.auth;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ProtectedTestController {

    @GetMapping("/employee")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    String testEmployee(){
        return "You're employee";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String testAdmin(){
        return "You're admin";
    }

    @GetMapping("/owner")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    String testOwner(){
        return "You're owner";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    String testUser(){
        return "You're user";
    }

}
