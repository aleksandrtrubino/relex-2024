package ru.trubino.farm.authority;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.trubino.farm.authority.exception.AuthorityNotFoundException;
import ru.trubino.farm.user.User;
import ru.trubino.farm.user.UserRepository;
import ru.trubino.farm.user.exception.UserNotFoundException;

import java.util.List;
import java.util.Set;

@Service
public class AuthorityService {

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    UserRepository userRepository;

    public User grantAuthorityToUser(Long authorityId, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with id: " + userId + " not found"));
        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(()-> new AuthorityNotFoundException("Authority with id: " + authorityId + " not found"));

        user.getAuthorities().add(authority);
        return userRepository.save(user);
    }

    public User revokeAuthorityFromUser(Long authorityId, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with id: " + userId + " not found"));
        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(()-> new AuthorityNotFoundException("Authority with id: " + authorityId + " not found"));

        user.getAuthorities().remove(authority);
        return userRepository.save(user);
    }

    public List<Authority> findAllAuthorities(){
        return authorityRepository.findAll();
    }

    public Set<User> findUsersByAuthorityId(Long authorityId){
        return authorityRepository.findById(authorityId)
                .orElseThrow(()-> new AuthorityNotFoundException("Authority  with id "+authorityId+" not found"))
                .getUsers();
    }

}
