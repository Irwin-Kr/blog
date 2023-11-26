package config.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import config.jwt.JwtFactory;
import config.jwt.JwtProperties;
import config.domain.RefreshToken;
import config.domain.User;
import config.dto.CreateAccessTokenRequest;
import config.repository.RefreshTokenRepository;
import config.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import java.util.Map;


@SpringBootTest
@AutoConfigureMockMvc
class TokenApiControllerTest {

	@Autowired
	protected MockMvc mockMvc;
	
	@Autowired
	protected ObjectMapper objectMapper;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	JwtProperties jwtProperties;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RefreshTokenRepository refreshTokenRepo;
	
	@BeforeEach
	public void mockMvcSetUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		userRepo.deleteAll();
	}
	
	@DisplayName("createNewAccessToken : 새로운 토큰 발급")
	@Test
	public void createNewAccessToken() throws Exception{
		//given
		final String url = "/api/token";
		
		User testUser = userRepo.save(User.builder().email("test@test.org").password("test").build());
		String refreshToken = JwtFactory.builder().claims(Map.of("id", testUser.getId())).build().createToken(jwtProperties);
		
		refreshTokenRepo.save(new RefreshToken(testUser.getId(), refreshToken));
		
		CreateAccessTokenRequest req = new CreateAccessTokenRequest();
		req.setRefreshToken(refreshToken);
		
		final String reqBody = objectMapper.writeValueAsString(req);
		
		//when
		ResultActions result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(reqBody));
		
		// then
		result.andExpect(status().isCreated()).andExpect(jsonPath("$.accessToken").isNotEmpty());
	}
	
}
