package cart.ui.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import cart.application.repository.CouponRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:reset.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class CouponReadControllerTest {

    @Autowired
    private CouponRepository couponRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("현재 사용자가 가지고 있는 쿠폰을 조회한다.")
    void findCoupons() {
        String email = "leotest@gmail.com";
        String password = "leo1234";

        String base64Credentials = java.util.Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", "Basic " + base64Credentials)
                .when().get("/coupons")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
