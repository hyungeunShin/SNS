package com.example.userserver.service;

import com.example.userserver.domain.User;
import com.example.userserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Transactional
    public User signUp(String username, String email, String plainPassword) {
        if(repository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username duplicated");
        }

        String hashedPassword = encoder.encode(plainPassword);
        return repository.save(new User(username, email, hashedPassword));
    }

    public User getUser(Long userId) {
        return repository.findById(userId).orElseThrow(NullPointerException::new);
    }

    public User getUserByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(NullPointerException::new);
    }

    public User signIn(String username, String plainPassword) {
        if(StringUtils.hasText(username)) {
            User user = repository.findByUsername(username).orElseThrow(NullPointerException::new);

            if(encoder.matches(plainPassword, user.getPassword())) {
                return user;
            }
        }
        throw new RuntimeException();
    }
}
