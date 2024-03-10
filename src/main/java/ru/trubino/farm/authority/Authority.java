package ru.trubino.farm.authority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import ru.trubino.farm.user.User;

import java.util.Set;

@Entity(name="authorities")
public class Authority implements GrantedAuthority {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String authority;
    @Getter
    @JsonIgnore
    @ManyToMany(mappedBy = "authorities")
    private Set<User> users;

    @Override
    public String getAuthority() {
        return authority;
    }
}
