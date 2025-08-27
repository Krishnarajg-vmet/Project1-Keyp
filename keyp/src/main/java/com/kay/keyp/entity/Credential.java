package com.kay.keyp.entity;



import java.time.LocalDateTime;

import com.kay.keyp.enums.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="credentials")
public class Credential {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="credential_id")
	private Long credentialId;
	
	@NotNull(message = "Account Type is required")
	@Enumerated(EnumType.STRING)
	@Column(name="account_type", nullable=false)
	private AccountType accountType;
	
	@NotBlank(message = "Username / Email is required")
	@Column(name="username", nullable=false)
	private String username;
	
	@NotBlank(message = "Password is required")
	@Column(name="password", nullable=false)
	private String password;
	
	@Column(name="created_at", nullable=false)
	private LocalDateTime createdAt;

	@Column(name="last_modified")
	private LocalDateTime lastModified;
	
	private Integer passwordStrength;
	
	private Integer reminderInDays;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private Users user;
	
	@Column(name="is_active")
	private Boolean isActive=true;
	
	@PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModified = LocalDateTime.now();
    }

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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(LocalDateTime lastModified) {
		this.lastModified = lastModified;
	}

	public Integer getPasswordStrength() {
		return passwordStrength;
	}

	public void setPasswordStrength(Integer passwordStrength) {
		this.passwordStrength = passwordStrength;
	}

	public Integer getReminderInDays() {
		return reminderInDays;
	}

	public void setReminderInDays(Integer reminderInDays) {
		this.reminderInDays = reminderInDays;
	}

	public Users getUsers() {
		return user;
	}

	public void setUsers(Users user) {
		this.user = user;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	

}
