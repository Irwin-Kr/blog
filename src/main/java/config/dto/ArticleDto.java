package config.dto;

import config.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ArticleDto {
	
	private String title;
	private String content;
	
	public Article toEntity() {
		return Article.builder().title(title).content(content).build();
	}

	public ArticleDto(Article article) {
		this.title = article.getTitle();
		this.content = article.getContent();
	}
	
	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}

}
