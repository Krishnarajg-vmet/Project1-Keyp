package com.kay.keyp.service;

import com.kay.keyp.dto.UsersDto;
import com.kay.keyp.entity.Users;
import com.kay.keyp.repository.UsersRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    public void validateUniqueEmail(String email, Long currentUserId) {
        Optional<Users> existingUser = usersRepository.findByEmail(email);
        if (existingUser.isPresent() && !existingUser.get().getUserId().equals(currentUserId)) {
            throw new RuntimeException("Email already in use");
        }
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
    
    public Users updateUser(Users user) {
        return usersRepository.save(user);
    }

    public Optional<Users> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public Optional<Users> findById(Long userId) {
        return usersRepository.findById(userId);
    }

 
    public UsersDto toDto(Users user) {
        UsersDto dto = new UsersDto();
        dto.setId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }


    public Users toEntity(UsersDto dto) {
        Users user = new Users();
        user.setUserId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }
    
    public boolean resetPassword(String username, String email, String newPassword) {
        Optional<Users> optionalUser = usersRepository.findByUsernameAndEmail(username, email);
        if (optionalUser.isEmpty()) {
            return false;
        }

        Users user = optionalUser.get();
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        usersRepository.save(user);
        return true;
    }
}
