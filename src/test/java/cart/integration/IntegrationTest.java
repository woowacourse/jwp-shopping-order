package cart.integration;

import cart.dto.MemberCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    ExtractableResponse<Response> requestCreateMember(String email, String password) {
        MemberCreateRequest request = new MemberCreateRequest(email, password);
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/members")
                .then().extract();
    }

    ExtractableResponse<Response> requestShowMemberPoint(String email, String password) {
        return RestAssured
                .given()
                .auth().preemptive().basic(email, password)
                .when()
                .get("members/points")
                .then().log().all()
                .extract();
    }
}
