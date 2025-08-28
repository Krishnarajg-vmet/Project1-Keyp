package com.kay.keyp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.kay.keyp.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
	
	@Bean 
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
		http 
			.authorizeHttpRequests(auth -> auth 
					.requestMatchers("/public/**", "/login", "/reset-password").permitAll() 
					.anyRequest().authenticated() 
					) 
			.formLogin(form -> form 
					.loginPage("/login") 
					.defaultSuccessUrl("/home", true) 
					.failureHandler((request, response, exception) -> { 
						exception.printStackTrace();
						response.sendRedirect("/login?error=true"); }) .permitAll()
					) 
			.logout(logout -> logout 
					.logoutUrl("/logout") 
					.logoutSuccessUrl("/login?logout=true") 
					.invalidateHttpSession(true) 
					.clearAuthentication(true) 
					.deleteCookies("JSESSIONID") 
					.permitAll() ) 
			.sessionManagement(session -> session 
					.sessionFixation().migrateSession() 
					.invalidSessionUrl("/sessionInvalid") 
					.maximumSessions(1) .maxSessionsPreventsLogin(false) 
					.expiredUrl("/sessionExpired") ); 
		return http.build(); 
		} 
	
	@Bean 
	public PasswordEncoder passEncode() { 
		return new BCryptPasswordEncoder();
		}
	
	@Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passEncode())
                .and()
                .build();
    }


}
