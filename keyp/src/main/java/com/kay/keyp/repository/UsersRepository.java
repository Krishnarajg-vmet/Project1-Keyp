package com.kay.keyp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kay.keyp.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{
	
	Optional<Users> findByUsername(String username);
	Optional<Users> findByEmail(String email);
	
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);

}
