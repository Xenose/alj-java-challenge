package jp.co.axa.apidemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ApiDemoApplicationTests {
	private final String localhost = "localhost";
	private final String port = "8080";
	@Autowired
	MockMvc mvc;
	@Test
	public void contextLoads() throws Exception {
	}

	@Test
	public void testHealth() throws Exception {
		String response = mvc.perform(get("/api/v1/health")).andReturn().getResponse().getContentAsString();

		if (!response.equals("200 OK")) {
			throw new Exception(
					"Did not get the right response jp.co.axa.apidemo @ ApiDemoApplicationTests.java :: message :: " +
							response);
		}

		System.out.println("Passed test");
	}

	/*
	 * Here we would tests as deleting, adding and listing users using the token to authorize ous.
	 */
}