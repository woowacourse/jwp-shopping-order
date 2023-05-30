package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import cart.dto.TotalDiscountInfoResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class DiscountIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("원래 가격을 통해 할인 정보를 응답한다.")
    void getPriceInfo() {
        final int originPrice = 10000;
        final String grade = "gold";
        final ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/discount?price=" + originPrice + "&memberGrade=" + grade)
                .then()
                .extract();

        final var result = response.as(TotalDiscountInfoResponse.class).getDiscountInformation();

        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(result.size()).isEqualTo(2),
                () -> assertThat(result.get(1).getPolicyName()).isEqualTo("priceDiscount"),
                () -> assertThat(result.get(1).getDiscountRate()).isEqualTo(0.02),
                () -> assertThat(result.get(1).getDiscountPrice()).isEqualTo(200),
                () -> assertThat(result.get(0).getPolicyName()).isEqualTo("memberGradeDiscount"),
                () -> assertThat(result.get(0).getDiscountRate()).isEqualTo(0.05),
                () -> assertThat(result.get(0).getDiscountPrice()).isEqualTo(500)
        );
    }
}
