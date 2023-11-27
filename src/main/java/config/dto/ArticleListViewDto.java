package config.dto;

import java.time.LocalDateTime;

import config.domain.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleListViewDto {
	
	private  long id;
	private  String title;
	private  String content;
	private String author;
	private  LocalDateTime regDt;
	
	public ArticleListViewDto(Article article) {
		this.id = article.getId();
		this.title = article.getTitle();
		this.content = article.getContent();
		this.author = article.getAuthor();
		this.regDt = article.getReg_Dt();
	}

}
