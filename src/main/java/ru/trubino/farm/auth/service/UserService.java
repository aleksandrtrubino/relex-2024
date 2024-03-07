package ru.trubino.farm.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.trubino.farm.auth.exception.EmailTakenException;
import ru.trubino.farm.auth.model.User;
import ru.trubino.farm.auth.payload.request.RegisterRequest;
import ru.trubino.farm.auth.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User createUser(RegisterRequest registerRequest){
        String email = registerRequest.getEmail();
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        String firstName = registerRequest.getFirstName();
        String middleName = registerRequest.getMiddleName();;
        String lastName = registerRequest.getLastName();

        if(userRepository.existsByEmail(email))
            throw new EmailTakenException("Email " + email + " is taken");


//        Role roleUser;
//        if(!roleRepository.existsByName("user"))
//            roleUser = roleRepository.save(new Role("user"));
//        else
//            roleUser = roleRepository.findByName("user").get();
//
//        Set<Role> defaultRoles = new HashSet<>();
//        defaultRoles.add(roleUser);

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .build();

        return userRepository.save(user);
    }
}
