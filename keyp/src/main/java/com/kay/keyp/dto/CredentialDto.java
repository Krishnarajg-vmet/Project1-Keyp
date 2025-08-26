package com.kay.keyp.dto;

import com.kay.keyp.enums.AccountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CredentialDto {
	
	private Long credentialId;
	
	@NotNull(message = "Account Type is required")
	private AccountType accountType;
	
	@NotBlank(message = "Username / Email is required")
	private String username;
	
	@NotBlank(message = "Password is required")
	private String password;
	
	private Integer reminderInDays;

	public Long getCredentialId() {
		return credentialId;
	}

	public void setCredentialId(Long credentialId) {
		this.credentialId = credentialId;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getReminderInDays() {
		return reminderInDays;
	}

	public void setReminderInDays(Integer reminderInDays) {
		this.reminderInDays = reminderInDays;
	}

	

}
