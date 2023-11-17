package config.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import config.service.BlogService;
import config.domain.Article;
import config.dto.ArticleDto;

@RequiredArgsConstructor
@RestController
public class BlogController {
	
	private final BlogService blogService;
	
	@GetMapping("/articles")
	public ResponseEntity<List<ArticleDto>> findAllArticles(){
		List<ArticleDto> articles = blogService.findAll().stream().map(ArticleDto::new).toList();
		
		return ResponseEntity.ok().body(articles);
	}
	
	@PostMapping("/additional/article")
	public ResponseEntity<Article> addArticle(@RequestBody ArticleDto dto ){
		Article addArticle = blogService.save(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(addArticle);
	}

}
