package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import config.jwt.TokenProvider;
import config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import config.oauth.OAuth2SuccessHandler;
import config.oauth.OAuth2UserCustomService;
import config.repository.RefreshTokenRepository;
import config.service.UserService;
import lombok.RequiredArgsConstructor;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {

	private final OAuth2UserCustomService oAuth2UserCustomService;
	private final TokenProvider tokenProvider;
	private final RefreshTokenRepository refreshTokenRepo;
	private final UserService userService;
	
	@Bean
	WebSecurityCustomizer configure() {
		return (web) -> web.ignoring().requestMatchers(toH2Console())
														 .requestMatchers(new AntPathRequestMatcher("/img/**"))
														 .requestMatchers(new AntPathRequestMatcher("/css/**"))
														 .requestMatchers(new AntPathRequestMatcher("/js/**"));
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
		
		httpSecurity.csrf((csrf) -> csrf.disable());
		httpSecurity.httpBasic((httpBasic) -> httpBasic.disable());
		httpSecurity.logout((logout) -> logout.disable());
		
		httpSecurity.sessionManagement((manager) -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		httpSecurity.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
		httpSecurity.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests.requestMatchers(new AntPathRequestMatcher("/additional/token")).permitAll()
																															   .requestMatchers(new AntPathRequestMatcher("/additional/**")).authenticated().anyRequest().permitAll());
	
		httpSecurity.oauth2Login(login -> login.loginPage("/login").authorizationEndpoint(authEndpoint -> authEndpoint.authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())).successHandler(oAuth2SuccessHandler()).userInfoEndpoint(userEndpoint -> userEndpoint.userService(oAuth2UserCustomService)));
		
		httpSecurity.logout(logout -> logout.logoutSuccessUrl("/login"));
		
		httpSecurity.exceptionHandling(handling -> handling.defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/additional/**")));
		
		return httpSecurity.build();
	}
	
	@Bean
	OAuth2SuccessHandler oAuth2SuccessHandler() {
		return new OAuth2SuccessHandler(tokenProvider, refreshTokenRepo, oAuth2AuthorizationRequestBasedOnCookieRepository(), userService);
	}
	
	@Bean
	TokenAuthenicationFilter tokenAuthenticationFilter() {
		return new TokenAuthenicationFilter(tokenProvider);
	}
	
	@Bean
	OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
		return new OAuth2AuthorizationRequestBasedOnCookieRepository();
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
