package ru.trubino.farm.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import ru.trubino.farm.authority.Authority;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity(name = "users")
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String password;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean credentialsNonExpired;
    private Boolean accountNonLocked;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_authorities",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "authority_id", referencedColumnName = "id"))
    private Set<Authority> authorities;

    @Column(unique = true)
    @Getter
    private String email;
    @Getter
    private String firstName;
    @Getter
    private String middleName;
    @Getter
    private String lastName;


    public User(String password, Boolean enabled, Boolean accountNonExpired, Boolean credentialsNonExpired, Boolean accountNonLocked,
                Set<Authority> authorities, String email, String firstName, String middleName, String lastName) {
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    @Builder
    public User(String password,String firstName, String middleName, String lastName, String email){
        this(password,true,true,true,true,null, email, firstName, middleName, lastName);
        this.authorities = new HashSet<>();
    }

    @Override
    public Set<Authority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return Long.toString(id);
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
