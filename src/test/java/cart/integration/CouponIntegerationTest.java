package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.request.CouponCreateRequest;
import cart.dto.response.CouponIssuableResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Base64;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CouponIntegerationTest extends IntegrationTest {

    private Member member;

    @Autowired
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        super.setUp();
        member = memberDao.getMemberById(1L);
    }

    @Test
    @DisplayName("등록된 쿠폰을 사용자가 발급받는다.")
    void publishUserCoupon() {
        CouponCreateRequest couponRequest = new CouponCreateRequest(1L);
        String encoded = new String(Base64.getEncoder().encode("a@a:123".getBytes()));

        given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + encoded)
                .body(couponRequest)
                .when().post("/users/coupons")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("사용자가 가진 쿠폰을 조회한다.")
    void getUserCoupons() {
        CouponCreateRequest couponRequest = new CouponCreateRequest(1L);
        String encoded = new String(Base64.getEncoder().encode("a@a:123".getBytes()));

        given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + encoded)
                .body(couponRequest)
                .when().post("/users/coupons")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + encoded)
                .when().get("/users/coupons")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("쿠폰을 조회한다.")
    void getCoupons() {
        String encoded = new String(Base64.getEncoder().encode("a@a:123".getBytes()));

        ExtractableResponse<Response> response = given().log().uri()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "basic " + encoded)
                .when().get("/coupons")
                .then()
                .statusCode(HttpStatus.OK.value()).extract();

        List<CouponIssuableResponse> couponResponses = response.jsonPath().getList(".", CouponIssuableResponse.class);

        assertAll(
                () -> assertThat(couponResponses.get(0).getName()).isEqualTo("5000원 할인 쿠폰"),
                () -> assertThat(couponResponses.get(0).getDiscountType()).isEqualTo("deduction")
        );
    }
}
