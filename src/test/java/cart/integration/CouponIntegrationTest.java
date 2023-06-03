package cart.integration;

import cart.dto.MemberCouponRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
}
