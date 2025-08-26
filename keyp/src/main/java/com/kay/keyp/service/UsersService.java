package com.kay.keyp.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kay.keyp.entity.Users;
import com.kay.keyp.repository.UsersRepository;

@Service
public class UsersService {
	
	private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    public Users registerUser(Users user) {
        if (usersRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        if (usersRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return usersRepository.save(user);
    }

    public Optional<Users> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public Optional<Users> findById(Long userId) {
        return usersRepository.findById(userId);
    }

}
