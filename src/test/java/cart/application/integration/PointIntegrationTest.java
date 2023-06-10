package cart.application.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class PointIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("포인트를 조회할 수 있다")
    public void getPoint() {
        // given
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic("a@a.com", "1234")
                // when
                .when()
                .get("/point")
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body().jsonPath().getLong("point")).isEqualTo(20000L);
    }
}
