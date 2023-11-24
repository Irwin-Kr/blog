package config.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import config.domain.User;
import config.repository.UserRepository;
import io.jsonwebtoken.Jwts;

@SpringBootTest
public class TokenProviderTest {

	@Autowired
	private TokenProvider tokenProvider;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private JwtProperties jwt;
	
	@DisplayName("generateToken() : 유저 정보와 만료 기간을 전달하는 토큰")
	@Test
	void generateToken() {
		//given
		User testUser = userRepo.save(User.builder().email("test@test.org").password("test").build());
		
		//when
		String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));
		
		//then
		Long userId = Jwts.parser().setSigningKey(jwt.getSecretKey()).parseClaimsJws(token).getBody().get("id", Long.class);
		
		assertThat(userId).isEqualTo(testUser.getId());
	}
	
	@DisplayName("validToken() : 만료된 토큰의 유효성 검증 실패")
	@Test
	void validToken_invalidToken() {
		//given
		String token = JwtFactory.builder().expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis())).build().createToken(jwt);
		
		//when
		boolean result = tokenProvider.validToken(token);
		
		//then
		assertThat(result).isFalse();
	}
	
	@DisplayName("validToken() : 유효한 토큰 검증 성공")
	@Test
	void validToken_validToken() {
		//given
		String token = JwtFactory.widthDefaultValues().createToken(jwt);
		
		//when
		boolean result = tokenProvider.validToken(token);
		
		//then
		assertThat(result).isTrue();
	}

	@DisplayName("getAuthentication : 토큰 기반으로 인증 정보 가져오기")
	@Test
	void gertAuthentication() {
		//given
		String email = "test@test.org";
		String token = JwtFactory.builder().subject(email).build().createToken(jwt);
		
		//when
		Authentication auth = tokenProvider.getAuthentication(token);
		
		//then
		assertThat(((UserDetails) auth.getPrincipal()).getUsername()).isEqualTo(email);
	}
	
	@DisplayName("getUserId() : 토큰으로 userId 가져오기")
	@Test
	void getUserId() {
		//given 
		Long userId = 1L;
		String token = JwtFactory.builder().claims(Map.of("id", userId)).build().createToken(jwt);
		
		//when
		Long userIdByToken = tokenProvider.getUserId(token);
		
		//then
		assertThat(userIdByToken).isEqualTo(userId);
	}
	
}
