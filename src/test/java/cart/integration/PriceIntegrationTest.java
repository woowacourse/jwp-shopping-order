package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import cart.domain.Member;
import cart.fixture.Fixture;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class PriceIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("원래 가격을 통해 할인 정보를 응답한다.")
    void getPriceInfo() {
        final int originPrice = 20000;
        final Member member = Fixture.GOLD_MEMBER;
        final ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .get("/prices?price=" + originPrice)
                .then()
                .extract();

        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getInt("gradeDiscount.discountRate")).isEqualTo(5),
                () -> assertThat(response.jsonPath().getInt("gradeDiscount.discountPrice")).isEqualTo(1000),
                () -> assertThat(response.jsonPath().getInt("priceDiscount.discountRate")).isEqualTo(3),
                () -> assertThat(response.jsonPath().getInt("priceDiscount.discountRate")).isEqualTo(600)
        );
    }
}
