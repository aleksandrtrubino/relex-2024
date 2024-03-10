package ru.trubino.farm.auth;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.trubino.farm.auth.exception.CookieNotFoundException;
import ru.trubino.farm.user.User;
import ru.trubino.farm.user.UserRepository;
import ru.trubino.farm.user.exception.UserNotFoundException;


@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;


    public JwtDto openSession(HttpServletResponse response, AuthDto authDto){
        String email = authDto.getEmail();
        String password = authDto.getPassword();

        User user = userRepository.findByEmail(email)
                        .orElseThrow(()-> new UserNotFoundException("User with email "+email+" not found"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),password));

        String refreshToken = jwtUtil.generateRefreshToken(user);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setMaxAge(jwtUtil.refreshTokenExpiration/1000);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        String accessToken = jwtUtil.generateAccessToken(user);

        return new JwtDto(accessToken);
    }

    public JwtDto extendSession(HttpServletRequest request){
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if(cookies == null)
            throw new CookieNotFoundException("Request does not include any cookies");
        for (Cookie c : cookies) {
            if (c.getName().equals("refreshToken"))
                refreshToken = c.getValue();
        }
        if(refreshToken == null )
            throw new CookieNotFoundException("Cookie 'refreshToken' not found");

        String username = jwtUtil.getSubject(refreshToken);
        User user = userDetailsService.loadUserByUsername(username);

        String accessToken = jwtUtil.generateAccessToken(user);

        return new JwtDto(accessToken);
    }

    public void closeSession(HttpServletResponse response){
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        response.addCookie(refreshTokenCookie);
    }
}
