package config.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.domain.Article;
import config.dto.ArticleDto;
import config.repository.BlogRepository;

@SpringBootTest
@AutoConfigureMockMvc
class BlogControllerTest {

	@Autowired
	protected MockMvc mockMvc;
	
	@Autowired
	protected ObjectMapper objectMapper;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	BlogRepository blogRepository;
	
	@BeforeEach
	public void mockMvcSetUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		blogRepository.deleteAll();
		
	}
	
	@DisplayName("addArticle : 글 추가에 성공하였습니다.")
	@Test
	public void addArticle() throws Exception{
		
		// given : 필요한 요청 객체 생성
		final String url = "/additional/article";
		final String title = "title";
		final String content = "content";
		final ArticleDto dto = new ArticleDto(title, content);
		
		// JSON으로 직렬화
		final String requestBody = objectMapper.writeValueAsString(dto);
		
		// when : JSON으로 요청, given에서 생성된 객체를 요청 본문으로 전송
		ResultActions result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody));
		
		// then : 응답코드 201 확인, 실제 저장된 데이터와 요청값 비교
		result.andExpect(status().isCreated());
		
		List<Article> articles = blogRepository.findAll();
		
		assertThat(articles.size()).isEqualTo(1);
		assertThat(articles.get(0).getTitle()).isEqualTo(title);
		assertThat(articles.get(0).getContent()).isEqualTo(content);
	}
	
	@DisplayName("findAllArticle : 전체 글 목록 조회")
	@Test
	public void findAllArticle() throws Exception{
		// given 
		final String url = "/articles";
		final String title = "title";
		final String content = "content";
		
		blogRepository.save(Article.builder().title(title).content(content).build());
		
		// when
		final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk()).andExpect(jsonPath("$[0].content").value(content)).andExpect(jsonPath("$[0].title").value(title));
	}
	
}
