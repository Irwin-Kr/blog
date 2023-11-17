package config.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
	
	// 블로그 글 조회
	public List<Article> findAll(){
		return blogRepo.findAll();
	}
	
}
