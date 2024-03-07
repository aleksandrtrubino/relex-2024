package ru.trubino.farm.auth.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AssignRoleRequest {
    String email;
    String role;
}
