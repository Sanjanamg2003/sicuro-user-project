package com.sicuro.solution.userproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

 @Bean
 public PasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder();
 }

 @Bean
 public UserDetailsService userDetailsService(PasswordEncoder encoder) {
     UserDetails admin = User.builder()
         .username("admin") 
         .password(encoder.encode("securepassword")) 
         .roles("ADMIN")
         .build();

     return new InMemoryUserDetailsManager(admin);
 }

 @Bean
 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     http
         .csrf(csrf -> csrf.disable()) 
         .authorizeHttpRequests(auth -> auth
    
             .requestMatchers("/api/user/**").authenticated() 
             
             .requestMatchers("/**").permitAll() 
         )
         .httpBasic(Customizer.withDefaults()); 

     return http.build();
 }
}