package cart.member;

import cart.config.IntegrationTest;
import cart.member.ui.request.DepositRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class MemberIntegrationTest extends IntegrationTest {

    @Test
    void 특정_유저의_캐시를_충전하다() {
        final DepositRequest request = DepositRequest.from(5000L);

        final ExtractableResponse<Response> response = given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/members/cash")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 특정_유저의_캐시를_확인하다() {
        final ExtractableResponse<Response> response = given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/cash")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
