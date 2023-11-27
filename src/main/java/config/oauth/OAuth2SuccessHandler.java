package config.oauth;

import java.io.IOException;
import java.time.Duration;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import config.domain.RefreshToken;
import config.domain.User;
import config.jwt.TokenProvider;
import config.repository.RefreshTokenRepository;
import config.service.UserService;
import config.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

	public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
	public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
	public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(2);
	public static final String REDIRECT_PATH = "/article";
	
	private final TokenProvider tokenProvider;
	private final RefreshTokenRepository refreshTokenRepo;
	private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepo;
	private final UserService userSerivce;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		User user = userSerivce.findByEmail((String) oAuth2User.getAttributes().get("email"));
		
		String refresh_Token = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
		saveRefreshToken(user.getId(), refresh_Token);
		addRefreshTokenToCookie(request, response, refresh_Token);
		
		String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
		String targetUrl = getTargetUrl(accessToken);
		
		clearAuthenticationAttributes(request, response);
		
		
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	private void saveRefreshToken(Long userId, String newRefreshToken) {
		RefreshToken refreshToken = refreshTokenRepo.findByUserId(userId).map(entity -> entity.update(newRefreshToken)).orElse(new RefreshToken(userId, newRefreshToken));
		
		refreshTokenRepo.save(refreshToken);
	}
	
	private void addRefreshTokenToCookie(HttpServletRequest req, HttpServletResponse resp, String refreshToken) {
		int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
		CookieUtil.delCookie(resp, req, refreshToken);
		CookieUtil.addCookie(resp, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
	}
	
	private void clearAuthenticationAttributes(HttpServletRequest req, HttpServletResponse resp) {
		super.clearAuthenticationAttributes(req);
		authorizationRequestRepo.removeAuthorizationRequestCookies(req, resp);
	}
	
	private String getTargetUrl(String token) {
		return UriComponentsBuilder.fromUriString(REDIRECT_PATH).queryParam("token", token).build().toUriString();
	}
	
}
