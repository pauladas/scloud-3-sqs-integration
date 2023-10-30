package br.com.paulojof.controller;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class ActuatorTest extends AbstractControllerTest {

	private static final String ACTUATOR_HEALTH_CHECK_URL = "/v1/actuator/health";

	/*
	 * Tests
	 */
	@Test
	public void shouldReturnHealthCheck() throws Exception {
		whenCallActuatorHealthCheck();
		thenExpectOkStatus();
		thenExpectResponseBodyIsNotNull();
		thenExpectResponseBodyUpStatus();
	}

	/*
	 * GIVEN
	 */

	/*
	 * WHEN
	 */
	private void whenCallActuatorHealthCheck() throws Exception {
		response = mockMvc.perform(get(ACTUATOR_HEALTH_CHECK_URL))
				.andReturn()
				.getResponse();
	}

	/*
	 * THEN
	 */
	private void thenExpectResponseBodyUpStatus() throws UnsupportedEncodingException {
		assertTrue(response.getContentAsString().contains("\"status\":\"UP\""));
	}

}
