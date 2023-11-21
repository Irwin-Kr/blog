package config.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import config.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail(String email);
	
}
