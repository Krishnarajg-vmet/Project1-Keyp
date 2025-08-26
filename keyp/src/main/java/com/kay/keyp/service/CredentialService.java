package com.kay.keyp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kay.keyp.dto.CredentialDto;
import com.kay.keyp.entity.Credential;
import com.kay.keyp.entity.Users;
import com.kay.keyp.repository.CredentialRepository;

@Service
public class CredentialService {
	
	
	private CredentialRepository credentialRepository;
	private EncryptionService encryptionService;
	
	public CredentialService(CredentialRepository credentialRepository, EncryptionService encryptionService) {
		this.credentialRepository = credentialRepository;
		this.encryptionService = encryptionService;
	}
	
	private CredentialDto mapToDTO(Credential entity) {
		CredentialDto dto = new CredentialDto();
        dto.setCredentialId(entity.getCredentialId());
        dto.setUsername(entity.getUsername());
        dto.setAccountType(entity.getAccountType());
        dto.setReminderInDays(entity.getReminderInDays());
        dto.setPassword(encryptionService.decrypt(entity.getPassword()));
        return dto;
    }

	public List<CredentialDto> getAllCredentialByUsers(Users users){
		
		return credentialRepository.findByUsersOrderByLastModifiedDesc(users).stream()
				.map(this::mapToDTO)
				.collect(Collectors.toList());
		
	}
	
	public CredentialDto getByCredentialId(Long credentialId) {
		return credentialRepository.findById(credentialId)
				.map(this::mapToDTO)
				.orElse(null);
	}
	
	public void saveCredentials(CredentialDto dto, Users users) {
	    Credential credential;
	    if (dto.getCredentialId() != null) {
	        credential = credentialRepository.findById(dto.getCredentialId())
	            .orElse(new Credential());
	    } else {
	        credential = new Credential();
	    }
	    credential.setAccountType(dto.getAccountType());
	    credential.setUsername(dto.getUsername());
	    credential.setPassword(encryptionService.encrypt(dto.getPassword()));
	    credential.setReminderInDays(dto.getReminderInDays());
	    credential.setUsers(users);
	    credentialRepository.save(credential);
	}
	
	public void deactivateCredentials(Long credentialId) {
	    credentialRepository.findById(credentialId).ifPresent(credential -> {
	        credential.setIsActive(false);
	        credentialRepository.save(credential);
	    });
	}
}
