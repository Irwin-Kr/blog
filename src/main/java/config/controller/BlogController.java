package config.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@GetMapping("/articles/{id}")
	public ResponseEntity<ArticleDto> findArticle(@PathVariable Long id) {
		
		Article article = blogService.find(id);
		
		return ResponseEntity.ok().body(new ArticleDto(article));
	}
	
	
	@PostMapping("/additional/article")
	public ResponseEntity<Article> addArticle(@RequestBody ArticleDto dto, Principal principal ){
		Article addArticle = blogService.save(dto, principal.getName());
		return ResponseEntity.status(HttpStatus.CREATED).body(addArticle);
	}
	
	@DeleteMapping("/articles/{id}")
	public ResponseEntity<Void> deleteArticle(@PathVariable Long id){
		blogService.delete(id);
		
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/articles/{id}")
	public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody ArticleDto dto){
		Article updateArticle = blogService.update(id, dto);
		
		return ResponseEntity.ok().body(updateArticle);
	}

}
