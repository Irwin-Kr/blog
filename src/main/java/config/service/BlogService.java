package config.service;

import org.springframework.stereotype.Service;

import config.domain.Article;
import config.dto.ArticleDto;
import lombok.RequiredArgsConstructor;
import config.repository.BlogRepository;

@RequiredArgsConstructor
@Service
public class BlogService {

	private final BlogRepository blogRepo;
	
	public Article save(ArticleDto dto) {
		return blogRepo.save(dto.toEntity());
	}
	
}
