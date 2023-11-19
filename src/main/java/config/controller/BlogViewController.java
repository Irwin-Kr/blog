package config.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import config.service.BlogService;
import config.domain.Article;
import config.dto.ArticleListViewDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

	private final BlogService blogService;
	
	@GetMapping("/article")
	public String getArticles(Model model) {
		List<ArticleListViewDto> articls = blogService.findAll().stream().map(ArticleListViewDto::new).toList();
		
		model.addAttribute("articls", articls);
		
		return "articles";	
	}
	
	@GetMapping("/article/{id}")
	public String getArticle(@PathVariable Long id, Model model) {
		Article article = blogService.find(id);
		model.addAttribute("article", new ArticleListViewDto(article));
		
		return "article";
	}
	
	@GetMapping("/newArticle")
	public String newArticle(@RequestParam(required=false) Long id, Model model) {
		if(id == null) {
			model.addAttribute("article", new ArticleListViewDto());
		}else {
			Article article = blogService.find(id);
			model.addAttribute("article", new ArticleListViewDto(article));
		}
		
		return "newArticle";
	}
	
}
