package cart.ui.coupon;

import cart.domain.coupon.Coupon;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CouponReadControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }
    @Test
    @DisplayName("GET /coupon 사용자의 쿠폰을 조회한다.")
    void findCoupons() {
        String email = "dino@gmail.com";
        String password = "dino123";

        String base64Credentials = java.util.Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", "Basic " + base64Credentials)
                .when().get("/coupons")
                .then().log().all()
                .extract();
        List<Coupon> coupons = List.of(
                new Coupon(1L, "깜짝 쿠폰 -10%", 1000, 10, 0));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", Coupon.class).get(0)).usingRecursiveComparison().isEqualTo(coupons.get(0));
    }
}
