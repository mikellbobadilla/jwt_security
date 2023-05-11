package com.mikellbobadilla.jwt_security.config;

import com.mikellbobadilla.jwt_security.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

  private final UserRepository userRepository;

  public AppConfig(UserRepository userRepository){
    this.userRepository = userRepository;
  }
  /* UserDetailsService only have one method to implement 'loadUserByUsername' (anonymous class concept) */
  @Bean
  public UserDetailsService userDetailsService(){
    return username -> userRepository.findByUsername(username)
        .orElseThrow(()-> new UsernameNotFoundException("User doesn't exists!"));
  }

  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder(10);
  }

  @Bean
  public AuthenticationProvider authProvider(){
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder());
    provider.setUserDetailsService(userDetailsService());
    return provider;
  }

  @Bean
  public AuthenticationManager authManager(AuthenticationConfiguration conf) throws Exception {
    return conf.getAuthenticationManager();
  }
}
