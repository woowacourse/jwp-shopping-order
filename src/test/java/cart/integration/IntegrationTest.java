package cart.integration;

import static io.restassured.RestAssured.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import cart.domain.member.Member;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	protected final static <T> ExtractableResponse<Response> get(Member member, String endpoint) {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth().preemptive().basic(member.getEmail(), member.getPassword())
			.when()
			.get(endpoint)
			.then()
			.log().all()
			.statusCode(HttpStatus.OK.value())
			.extract();
	}

	protected final static <T> ExtractableResponse<Response> get(Member member, String endpoint, Long id,
		int statusCode) {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth().preemptive().basic(member.getEmail(), member.getPassword())
			.when()
			.get(endpoint, id)
			.then()
			.log().all()
			.statusCode(statusCode)
			.extract();
	}

	protected final static <T> ExtractableResponse<Response> post(Member member, T request, String endpoint,
		int statusCode) {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth().preemptive().basic(member.getEmail(), member.getPassword())
			.body(request)
			.when()
			.post(endpoint)
			.then()
			.log().all()
			.statusCode(statusCode)
			.extract();
	}

	protected final static <T> ExtractableResponse<Response> post(Member member, T request, String endpoint, Long id,
		int statusCode) {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth().preemptive().basic(member.getEmail(), member.getPassword())
			.body(request)
			.when()
			.post(endpoint, id)
			.then()
			.log().all()
			.statusCode(statusCode)
			.extract();
	}

	protected final static <T> ExtractableResponse<Response> patch(Member member, String endpoint, Long id,
		int statusCode) {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth().preemptive().basic(member.getEmail(), member.getPassword())
			.when()
			.patch(endpoint, id)
			.then()
			.log().all()
			.statusCode(statusCode)
			.extract();
	}

	protected final static <T> ExtractableResponse<Response> delete(Member member, String endpoint, Long id,
		int statusCode) {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth().preemptive().basic(member.getEmail(), member.getPassword())
			.when()
			.delete(endpoint, id)
			.then()
			.log().all()
			.statusCode(statusCode)
			.extract();
	}
}
