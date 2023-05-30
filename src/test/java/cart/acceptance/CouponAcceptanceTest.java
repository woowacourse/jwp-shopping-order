package cart.acceptance;

import cart.dto.CouponResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CouponAcceptanceTest extends AcceptanceTest {

    @Test
    void 쿠폰을_조회한다() {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().basic("pizza1@pizza.com", "pizza")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/coupons")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final List<CouponResponse> couponResponses = response.jsonPath().getList("", CouponResponse.class);
        final CouponResponse couponResponse = couponResponses.get(0);
        assertAll(
                () -> assertThat(couponResponses.size()).isEqualTo(2),
                () -> assertThat(couponResponse.getId()).isEqualTo(1),
                () -> assertThat(couponResponse.getName()).isEqualTo("30000원 이상 3000원 할인 쿠폰"),
                () -> assertThat(couponResponse.getType()).isEqualTo("PRICE"),
                () -> assertThat(couponResponse.getValue()).isEqualTo(3000),
                () -> assertThat(couponResponse.getMinimumPrice()).isEqualTo(30000)
        );
    }
}
