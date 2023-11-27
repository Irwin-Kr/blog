package config.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import config.dto.UserDto;
import config.domain.User;
import config.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepo;
	//private final BCryptPasswordEncoder bcrypt;
	
	public Long regist(UserDto userDto) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return userRepo.save(User.builder().email(userDto.getEmail()).password(encoder.encode(userDto.getPassword())).build()).getId();
		
	}
	
	public User findById(Long userId) {
		return userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
	}
	
	public User findByEmail(String email) {
		return userRepo.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("Unexpected user"));
	}
	
}
