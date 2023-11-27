package config.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import config.domain.Article;
import config.dto.ArticleDto;
import lombok.RequiredArgsConstructor;
import config.repository.BlogRepository;

@RequiredArgsConstructor
@Service
public class BlogService {

	private final BlogRepository blogRepo;
	
	// 블로그 저장
	public Article save(ArticleDto dto, String userName) {
		return blogRepo.save(dto.toEntity(userName));
	}
	
	// 블로그 글 전체 조회
	public List<Article> findAll(){
		return blogRepo.findAll();
	}
	
	// 글 단일 조회
	public Article find(long id) {
		return blogRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
	}
	
	// 글 삭제
	public void delete(long id) {
//		blogRepo.deleteById(id);
		Article article = blogRepo.findById(id).orElseThrow(() -> new  IllegalArgumentException("not found: " + id));
		
		authorizeArticleAuthor(article);
		blogRepo.delete(article);
		
	}
	
	// 글 수정
	@Transactional
	public Article update(long id, ArticleDto dto) {
		Article article = blogRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("not found : " + id));
		authorizeArticleAuthor(article);
//		article.update(dto.getTitle(), dto.getContent());
		article.update(dto.getTitle(), dto.getContent());
		
		return article;
	}
	
	private static void authorizeArticleAuthor(Article article) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		if(!article.getAuthor().equals(userName)) {
			throw new IllegalArgumentException("not found");
		}
	}
	
}
