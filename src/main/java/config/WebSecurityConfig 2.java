package config;
/*
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity.IgnoredRequestConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatchers;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import config.service.UserDetailService;
import lombok.RequiredArgsConstructor;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import java.util.Arrays;
import java.util.List;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	
	private final UserDetailService userService;
	
	@Bean
	public WebSecurityCustomizer webSecurityCustom() {
		return (web) -> web.ignoring().requestMatchers(toH2Console())
														.requestMatchers(new AntPathRequestMatcher("/static/**"))
														.requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		/*
		//http.authorizeHttpRequests((authorizeHttpRquestes) -> authorizeHttpRquestes.requestMatchers("/login", "/signup", "/user").permitAll().anyRequest().authenticated());
		try{
			//http.authorizeHttpRequests((authorizeHttpRequestes) -> authorizeHttpRequestes.requestMatchers(new MvcRequestMatcher(introspector, "/login")).permitAll().anyRequest().authenticated());
			//http.authorizeHttpRequests((authorizeHttpRequestes1) -> authorizeHttpRequestes1.requestMatchers(new MvcRequestMatcher(introspector, "/signup")).permitAll().anyRequest().authenticated());
			//http.authorizeHttpRequests((authorizeHttpRequestes2) -> authorizeHttpRequestes2.requestMatchers(new MvcRequestMatcher(introspector, "/user")).permitAll().anyRequest().authenticated());
			http.authorizeHttpRequests((authorizeHttpRequestes3) -> authorizeHttpRequestes3.requestMatchers("/login", "/signup", "/user").permitAll().anyRequest().authenticated());
			//http.securityMatchers((matchers) -> matchers.requestMatchers(new MvcRequestMatcher(introspector, "/login")).anyRequest());
			//http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests.anyRequest().permitAll());	
			//http.requestMatchers(new MvcRequestMatcher(introspector, "/login"), new MvcRequestMatcher(introspector, "/signup"), new MvcRequestMatcher(introspector, "/user")).permitAll().anyRequest().authenticated();
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		http.httpBasic(Customizer.withDefaults());
		http.formLogin((formLogin) -> formLogin.loginPage("/login").defaultSuccessUrl("/articles"));
		http.logout((logout) -> logout.logoutSuccessUrl("/login").invalidateHttpSession(true));
		http.csrf(AbstractHttpConfigurer::disable);
		return http.build();
		//
		http.csrf(AbstractHttpConfigurer::disable);
		//http.exceptionHandling((handling) -> handling.authenticationEntryPoint(null).accessDeniedHandler(null));
		http.headers((headers)->headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
		http.authorizeHttpRequests((registry) -> registry.requestMatchers(new AntPathRequestMatcher("/login")).permitAll().requestMatchers(new AntPathRequestMatcher("/signup")).permitAll().requestMatchers(new AntPathRequestMatcher("/user")).permitAll().anyRequest().authenticated());
		http.httpBasic(Customizer.withDefaults());
		http.formLogin((formLogin) -> formLogin.loginPage("/login").defaultSuccessUrl("/article"));
		http.logout((logout) -> logout.logoutSuccessUrl("/login").invalidateHttpSession(true));
		return http.build();
}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity security, BCryptPasswordEncoder bcrypt, UserDetailService userDetailService) throws Exception{
		return security.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(userService).passwordEncoder(bcrypt).and().build();
	}
	
	@Bean
	public BCryptPasswordEncoder bcrypt() {
		return new BCryptPasswordEncoder();
	}
	

}
*/