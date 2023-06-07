package cart.member.integration;

import cart.config.IntegrationTest;
import cart.member.ui.request.DepositRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static cart.fixtures.MemberFixtures.Member_Dooly;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class MemberIntegrationTest extends IntegrationTest {

    @Test
    void 특정_유저의_캐시를_충전하다() {
        // given
        final DepositRequest request = DepositRequest.from(5000L);

        // when
        final ExtractableResponse<Response> response = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(Member_Dooly.EMAIL, Member_Dooly.PASSWORD)
                .body(request)
                .when()
                .post("/members/cash")
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 특정_유저의_캐시를_확인하다() {
        // given, when
        final ExtractableResponse<Response> response = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(Member_Dooly.EMAIL, Member_Dooly.PASSWORD)
                .when()
                .get("/members/cash")
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
