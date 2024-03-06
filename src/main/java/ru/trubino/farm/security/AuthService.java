package ru.trubino.farm.security;


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
import ru.trubino.farm.security.exception.AbsentTokenException;
import ru.trubino.farm.security.exception.EmailTakenException;
import ru.trubino.farm.security.exception.InvalidTokenException;
import ru.trubino.farm.security.payload.request.LoginRequest;
import ru.trubino.farm.security.payload.request.RegisterRequest;
import ru.trubino.farm.security.payload.response.AccessTokenResponse;
import ru.trubino.farm.user.User;
import ru.trubino.farm.user.UserRepository;


@Service
public class AuthService {

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;


    public void register(RegisterRequest registerRequest){
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        String firstName = registerRequest.getFirstName();
        String middleName = registerRequest.getMiddleName();;
        String lastName = registerRequest.getLastName();

//        if(userRepository.existsByEmail(email))
//            throw new EmailTakenException("Email " + email + " is taken");

        userRepository.save(new User(email, passwordEncoder.encode(password), firstName, middleName, lastName));
    }

    public AccessTokenResponse login(HttpServletResponse response, LoginRequest loginRequest){
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        if(!userRepository.existsByEmail(email))
            throw new UsernameNotFoundException("User with email " + email + " doesn't exist");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        String refreshToken = jwtService.generateRefreshToken(userDetails);

        String userId = userRepository.findByEmail(email).get().getId().toString();

        addCookie(response, "refreshToken", refreshToken);
        addCookie(response, "email", email);

        String accessToken = jwtService.generateAccessToken(userDetails);

        return new AccessTokenResponse(accessToken);
    }

    private void addCookie(HttpServletResponse response, String cookieName, String cookieValue){
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(jwtService.refreshTokenExpiration/1000);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    public void logout(HttpServletResponse response){
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        response.addCookie(refreshTokenCookie);
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
        if(!jwtService.isTokenValid(refreshToken))
            throw new InvalidTokenException("Invalid access token");

        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String accessToken = jwtService.generateAccessToken(userDetails);

        return new AccessTokenResponse(accessToken);
    }

}
