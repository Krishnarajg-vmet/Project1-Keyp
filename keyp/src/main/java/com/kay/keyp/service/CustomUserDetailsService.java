package com.kay.keyp.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kay.keyp.entity.Users;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	 private final UsersService usersService;

	    public CustomUserDetailsService(UsersService usersService) {
	        this.usersService = usersService;
	    }
	    
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = usersService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
	}
	
	

}
