package config.service;

import java.util.List;

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
	public Article save(ArticleDto dto) {
		return blogRepo.save(dto.toEntity());
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
		blogRepo.deleteById(id);
	}
	
	// 글 수정
	@Transactional
	public Article update(long id, ArticleDto dto) {
		Article article = blogRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("not found : " + id));
		
		article.update(dto.getTitle(), dto.getContent());
		
		return article;
	}
	
}
