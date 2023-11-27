package config.util;

import java.util.Base64;

import org.springframework.util.SerializationUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
	
	// 요청값에 쿠키 추가(키, 값, 기간)
	public static void addCookie(HttpServletResponse resp, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		resp.addCookie(cookie);
	}
	
	// 쿠키의 키를 받아 삭제
	public static void delCookie(HttpServletResponse resp, HttpServletRequest req, String name) {
		Cookie[] cookies = req.getCookies();
		if(cookies==null) {
			return;
		}
		for(Cookie cookie:cookies) {
			if(name.equals(cookie.getName())) {
				cookie.setValue("");
				cookie.setPath("/");
				cookie.setMaxAge(0);
				
				resp.addCookie(cookie);
			}
		}
	}
	
	public static String serialize(Object obj) {
		return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj));
	}

	public static <T> T deserialize(Cookie cookie, Class<T> cls) {
		return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
	}
	
}
