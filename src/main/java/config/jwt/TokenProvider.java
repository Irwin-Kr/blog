package config.jwt;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import config.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenProvider {

	private final JwtProperties jwt;
	
	public String generateToken(User user, Duration expiredAt) {
		Date now = new Date();
		return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
	}
	
	private String makeToken(Date expiry, User user) {
		Date now = new Date();
		
		return Jwts.builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.setIssuer(jwt.getIssuer()).setIssuedAt(now).setExpiration(expiry).setSubject(user.getEmail()).claim("id", user.getId())
				.signWith(SignatureAlgorithm.HS256, jwt.getSecretKey()).compact();
	}
	
	public boolean validToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwt.getSecretKey()).parseClaimsJws(token);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public Authentication getAuthentication(String token) {
		Claims claims = getClaims(token);
		Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
		return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "" , authorities), token, authorities);
	}
	
	public Long getUserId(String token) {
		Claims claims = getClaims(token);
		return claims.get("id", Long.class);
	}
	
	private Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(jwt.getSecretKey()).parseClaimsJws(token).getBody();
	}
}
