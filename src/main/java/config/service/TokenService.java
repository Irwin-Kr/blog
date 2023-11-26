package config.service;

import java.time.Duration;

import config.domain.User;
import org.springframework.stereotype.Service;

import config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenService {

	private final TokenProvider tokenProvider;
	private final RefreshTokenService refreshTokenService;
	private final UserService userService;
	
	public String createNewAccessToken(String refreshToken) {
		if(!tokenProvider.validToken(refreshToken)) {
			throw new IllegalArgumentException("Unexpected token");
		}
		
		Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
		User user = userService.findById(userId);
		
		return tokenProvider.generateToken(user, Duration.ofHours(2));
	}
	
}
