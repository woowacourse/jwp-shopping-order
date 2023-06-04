package cart.integration;

import cart.dto.AllCouponResponse;
import cart.dto.CouponResponse;
import cart.dto.MemberCouponRequest;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static io.restassured.RestAssured.given;

@SuppressWarnings("ALL")
public class CouponIntegrationTest extends IntegrationTest {

    @Test
    void 쿠폰을_발급한다() {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic("a@a.com", "1234")
                .body("{\"expiredAt\": \"2099-12-31T00:00:00\"}")
                .when()
                .post("/coupons/{couponId}", 1L)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all();
    }

    @Test
    void 쿠폰_목록_조회한다() {
        CouponResponse 쿠폰1 = new CouponResponse(1L, "테스트쿠폰1", 10000, 3000, "FIXED_PERCENTAGE", null, 0.3);
        CouponResponse 쿠폰2 = new CouponResponse(2L, "테스트쿠폰2", 15000, null, "FIXED_AMOUNT", 2000, null);

        AllCouponResponse expected = new AllCouponResponse(List.of(쿠폰1, 쿠폰2));
        Response response = given().log().all()
                .when()
                .get("/coupons")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().response();
        AllCouponResponse allCouponResponse = response.getBody().as(AllCouponResponse.class);

        Assertions.assertThat(allCouponResponse).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
