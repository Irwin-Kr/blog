package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import config.service.UserService;
import lombok.RequiredArgsConstructor;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
	private final UserService userService;
	
	@Bean
	public WebSecurityCustomizer webSecurityCustom() {
		return (web) -> web.ignoring().requestMatchers(toH2Console()).requestMatchers("/static/**");
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity security) throws Exception{
		return security.authorizeRequests().requestMatchers("/login", "/signup", "/user").permitAll().anyRequest().authenticated()
				.and().formLogin().loginPage("/login").defaultSuccessUrl("/article")
				.and().logout().logoutSuccessUrl("/login").invalidateHttpSession(true)
				.and().csrf().disable().build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity security, BCryptPasswordEncoder bcrypt, UserService userService) throws Exception{
		return security.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(userService).passwordEncoder(bcrypt).and().build();
	}
	
	@Bean
	public BCryptPasswordEncoder bcrypt() {
		return new BCryptPasswordEncoder();
	}
	

}
