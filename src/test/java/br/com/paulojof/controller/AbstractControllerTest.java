package br.com.paulojof.controller;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("hiding")
@ExtendWith(SpringExtension.class)
public abstract class AbstractControllerTest<T> {

	@Autowired
	protected MockMvc mockMvc;

	protected MockHttpServletResponse response;

	protected void thenExpectOkStatus() {
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	protected void thenExpectCreatedStatus() {
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
	}

	protected void thenExpectNoContentStatus() {
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
	}

	protected void thenExpectUnprocessableEntityStatus() {
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
	}

	protected void thenExpectNotFoundStatus() {
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
	}

	protected void thenExpectBadRequestStatus() {
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	protected void thenExpectResponseBodyIsNotNull() {
		assertNotNull(response);
	}

	protected void thenExpectResponseHeaderLocationIsNotNull() {
		assertNotNull(response.getHeader("location"));
	}

	protected static List<String> blankStrings() {
		return Arrays.asList(null, StringUtils.EMPTY, StringUtils.SPACE);
	}

}
