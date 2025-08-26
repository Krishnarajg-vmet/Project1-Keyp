package com.kay.keyp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kay.keyp.entity.Credential;
import com.kay.keyp.entity.Users;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long>{

	List<Credential> findByUsersOrderByLastModifiedDesc(Users users);
}
