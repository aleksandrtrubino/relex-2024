package ru.trubino.farm.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.trubino.farm.user.exception.UserAlreadyExistsException;
import ru.trubino.farm.user.exception.UserNotFoundException;

import java.util.List;

@Service
public class UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User createUser(UserDto userDto){
        String email = userDto.email();
        String encodedPassword = passwordEncoder.encode(userDto.password());
        String firstName = userDto.firstName();
        String middleName = userDto.middleName();
        String lastName = userDto.lastName();

        if(userRepository.existsByEmail(email))
            throw new UserAlreadyExistsException("User with email " + email + " already exists");

        User appUser = User.builder()
                .email(email)
                .password(encodedPassword)
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .build();

        return userRepository.save(appUser);
    }

    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }

    public void disableById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        user.setEnabled(false);
        userRepository.save(user);
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User findUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public User updateUserById(Long id, UserDto userDto, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, boolean enabled) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User with id"+id+" not found"));
        user.setAccountNonExpired(accountNonExpired);
        user.setCredentialsNonExpired(credentialsNonExpired);
        user.setAccountNonLocked(accountNonLocked);
        user.setEnabled(enabled);

        if(userDto != null){
            String email = userDto.email();
            String encodedPassword = passwordEncoder.encode(userDto.password());
            String firstName = userDto.firstName();
            String middleName = userDto.middleName();
            String lastName = userDto.lastName();

            if(!email.equals(user.getEmail()) && userRepository.existsByEmail(email))
                throw new UserAlreadyExistsException("User with email " + email + " already exists");

            user.setEmail(email);
            user.setPassword(encodedPassword);
            user.setFirstName(firstName);
            user.setMiddleName(middleName);
            user.setLastName(lastName);
        }

        return userRepository.save(user);
    }

}
