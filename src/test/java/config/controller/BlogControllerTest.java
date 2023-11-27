package config.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.domain.Article;
import config.domain.User;
import config.dto.ArticleDto;
import config.repository.BlogRepository;
import config.repository.UserRepository;

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
	
	@Autowired
	UserRepository userRepo;
	
	User user;
	
	@BeforeEach
	public void mockMvcSetUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		blogRepository.deleteAll();
		
	}
	
	@BeforeEach
	void setSecurityContext() {
		userRepo.deleteAll();
		user = userRepo.save(User.builder().email("test@test.org").password("test").build());
		
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
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
		
		Principal principal = Mockito.mock(Principal.class);
		Mockito.when(principal.getName()).thenReturn("username");
		
		// when : JSON으로 요청, given에서 생성된 객체를 요청 본문으로 전송
		ResultActions result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).principal(principal).content(requestBody));
		
		// then : 응답코드 201 확인, 실제 저장된 데이터와 요청값 비교
		result.andExpect(status().isCreated());
		
		List<Article> articles = blogRepository.findAll();
		
		assertThat(articles.size()).isEqualTo(1);
		assertThat(articles.get(0).getTitle()).isEqualTo(title);
		assertThat(articles.get(0).getContent()).isEqualTo(content);
	}
	
	@DisplayName("findAllArticles : 전체 글 목록 조회")
	@Test
	public void findAllArticle() throws Exception{
		// given 
		final String url = "/articles";
		//final String title = "title";
		//final String content = "content";
		Article savedArticle = createDefaultArticle();
		
		//blogRepository.save(Article.builder().title(title).content(content).build());
		
		// when
		final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));
		
		//then
//		result.andExpect(status().isOk()).andExpect(jsonPath("$[0].content").value(content)).andExpect(jsonPath("$[0].title").value(title));
		result.andExpect(status().isOk()).andExpect(jsonPath("$[0].content").value(savedArticle.getContent())).andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()));
	}
	
	@DisplayName("findArticle : 글 단일 조회")
	@Test
	public void findArticle() throws Exception{
		
		// given
		final String url="/articles/{id}";
		//final String title = "title";
		//final String content = "content";
		Article savedArticle = createDefaultArticle();
		
		//Article findArticle = blogRepository.save(Article.builder().title(title).content(content).build());
		
		// when
		//final ResultActions result = mockMvc.perform(get(url, findArticle.getId()));
		final ResultActions result = mockMvc.perform(get(url, savedArticle.getId()));
		
		//then
//		result.andExpect(status().isOk()).andExpect(jsonPath("$.content").value(content)).andExpect(jsonPath("$.title").value(title));
		result.andExpect(status().isOk()).andExpect(jsonPath("$.content").value(savedArticle.getContent())).andExpect(jsonPath("$.title").value(savedArticle.getTitle()));
	}
	
	@DisplayName("deleteArticle : 글 삭제")
	@Test
	public void deleteArticle() throws Exception{
		
		// given
		final String url = "/articles/{id}";
		//final String title = "title";
		//final String content = "content";
		Article savedArticle = createDefaultArticle();
		//Article deleteArticle = blogRepository.save(Article.builder().title(title).content(content).build());
		
		// when
//		mockMvc.perform(delete(url, deleteArticle.getId())).andExpect(status().isOk());
		mockMvc.perform(delete(url, savedArticle.getId())).andExpect(status().isOk());
		
		// then
		List<Article> articles = blogRepository.findAll();
		
		assertThat(articles).isEmpty();
		
	}
	
	@DisplayName("updateArticle : 글 수정")
	@Test
	public void updateArticle() throws Exception{
		
		// given
		final String url = "/articles/{id}";
		Article savedArticle = createDefaultArticle();
		//final String title = "title";
		//final String content = "content";
		
//		Article saveArticle = blogRepository.save(Article.builder().title(title).content(content).build());
		
		final String newTitle = "new title";
		final String newContent = "new content";
		
		ArticleDto updateArticle  = new ArticleDto(newTitle, newContent);
		
		// when
//		ResultActions result = mockMvc.perform(put(url, saveArticle.getId()).contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(updateArticle)));
		ResultActions result = mockMvc.perform(put(url, savedArticle.getId()).contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(updateArticle)));
		
		// then
		result.andExpect(status().isOk());
		
//		Article article = blogRepository.findById(saveArticle.getId()).get();
		Article article = blogRepository.findById(savedArticle.getId()).get();
		
		assertThat(article.getTitle()).isEqualTo(newTitle);
		assertThat(article.getContent()).isEqualTo(newContent);
		
	}
	
	private Article createDefaultArticle() {
		return blogRepository.save(Article.builder().title("title").content("content").author(user.getUsername()).build());
	}
	
}
