package config.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", updatable = false)
	private Long id;
	
	@Column(name="title", nullable= false)
	private String title;
	
	@Column(name="content", nullable=false)
	private String content;
	
	@CreatedDate
	@Column(name = "reg_Dt")
	private LocalDateTime reg_Dt;
	
	@LastModifiedDate
	@Column(name ="mod_Dt")
	private LocalDateTime mod_Dt;
	
	@Builder
	public Article(String title, String content) {
		this.title = title;
		this.content = content;
	}
	
	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}
	
}
