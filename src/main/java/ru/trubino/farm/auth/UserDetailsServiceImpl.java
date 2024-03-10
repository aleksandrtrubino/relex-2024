package ru.trubino.farm.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.trubino.farm.user.User;
import ru.trubino.farm.user.UserRepository;
import ru.trubino.farm.user.exception.UserNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username){
        return userRepository.findById(Long.valueOf(username))
                .orElseThrow(() -> new UserNotFoundException("User with id "+username+" not found"));
    }
}
