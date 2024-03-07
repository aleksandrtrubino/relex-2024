package ru.trubino.farm.auth.service;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.trubino.farm.auth.exception.AbsentTokenException;
import ru.trubino.farm.auth.exception.EmailTakenException;
import ru.trubino.farm.auth.exception.InvalidTokenException;
import ru.trubino.farm.auth.payload.request.AssignRoleRequest;
import ru.trubino.farm.auth.payload.request.LoginRequest;
import ru.trubino.farm.auth.payload.request.RegisterRequest;
import ru.trubino.farm.auth.payload.request.CreateRoleRequest;
import ru.trubino.farm.auth.payload.response.AccessTokenResponse;
import ru.trubino.farm.auth.model.Role;
import ru.trubino.farm.auth.repository.RoleRepository;
import ru.trubino.farm.auth.model.User;
import ru.trubino.farm.auth.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;


@Service
public class AuthService {

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;


    public void register(RegisterRequest registerRequest){
        String email = registerRequest.getEmail();
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        String firstName = registerRequest.getFirstName();
        String middleName = registerRequest.getMiddleName();;
        String lastName = registerRequest.getLastName();

        if(userRepository.existsByEmail(email))
           throw new EmailTakenException("Email " + email + " is taken");


        Role roleUser;
        if(!roleRepository.existsByName("user"))
            roleUser = roleRepository.save(new Role("user"));
        else
            roleUser = roleRepository.findByName("user").get();

        Set<Role> defaultRoles = new HashSet<>();
        defaultRoles.add(roleUser);

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .roles(defaultRoles)
                .build();

        userRepository.save(user);
    }

    private void addCookie(HttpServletResponse response, String cookieName, String cookieValue){
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(jwtUtil.refreshTokenExpiration/1000);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    public AccessTokenResponse login(HttpServletResponse response, LoginRequest loginRequest){
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        if(!userRepository.existsByEmail(email))
            throw new UsernameNotFoundException("User with email " + email + " doesn't exist");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setMaxAge(jwtUtil.refreshTokenExpiration/1000);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        String accessToken = jwtUtil.generateAccessToken(userDetails);

        return new AccessTokenResponse(accessToken);
    }

    public AccessTokenResponse getAccessToken(HttpServletRequest request){
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if(cookies == null)
            throw new AbsentTokenException("No cookies at all");
        for (Cookie c : cookies) {
            if (c.getName().equals("refreshToken"))
                refreshToken = c.getValue();
        }
        if(refreshToken == null )
            throw new AbsentTokenException("Absent refresh token");
        if(!jwtUtil.isTokenValid(refreshToken))
            throw new InvalidTokenException("Invalid access token");

        String username = jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String accessToken = jwtUtil.generateAccessToken(userDetails);

        return new AccessTokenResponse(accessToken);
    }

    public void logout(HttpServletResponse response){
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        response.addCookie(refreshTokenCookie);
    }


    public Role createRole(CreateRoleRequest createRoleRequest){
        if(createRoleRequest.getName() == null)
            throw new RuntimeException("EmptyRole");
        if(roleRepository.existsByName(createRoleRequest.getName()))
            throw new RuntimeException("RoleAlreadyExists");
        return roleRepository.save(new Role(createRoleRequest.getName()));
    }

    public User assignRole(AssignRoleRequest request){
        String email = request.getEmail();
        String roleName = request.getRole();

        if(!roleRepository.existsByName(roleName))
            throw new RuntimeException("RoleDoesNotExistException");
        if(!userRepository.existsByEmail(email))
            throw new UsernameNotFoundException("User with email " + email + " not found");

        User user = userRepository.findByEmail(email).get();
        Role role = roleRepository.findByName(roleName).get();

        user.addRole(role);
        return userRepository.save(user);
    }

}
