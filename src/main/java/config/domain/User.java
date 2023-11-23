package config.domain;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", updatable=false)
	private Long id;
	
	@Column(name="email", nullable=false, unique=true)
	private String email;
	
	@Column(name="password")
	private String password;
	
	@Builder
	public User(String email, String password, String auth) {
		this.email = email;
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of(new SimpleGrantedAuthority("user"));
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}
	
	@Override
	public String getPassword() {
		return password;
	}

	// 계정 만료 여부
	@Override
	public boolean isAccountNonExpired() {
		// 만료 확인 로직
		return true;
	}

	// 계정 잠금 여부
	@Override
	public boolean isAccountNonLocked() {
		// 잠금 확인 로직
		return true;
	}

	// 패스워드 만료 여부
	@Override
	public boolean isCredentialsNonExpired() {
		// 패스워드 만료 확인 로직
		return true;
	}

	// 계정 사용 여부
	@Override
	public boolean isEnabled() {
		// 계정 사용 확인 로직
		return true;
	}
	
	

}
