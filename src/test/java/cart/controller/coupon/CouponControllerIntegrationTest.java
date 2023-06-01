package cart.controller.coupon;

import cart.dto.coupon.CouponCreateRequest;
import cart.service.coupon.CouponService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
class CouponControllerIntegrationTest {

    @Autowired
    private CouponService couponService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @DisplayName("쿠폰을 모두 조회한다.")
    @Test
    void find_all_coupons() {
        // given
        couponService.createCoupon(new CouponCreateRequest("쿠폰", true, 10));

        // when & then
        Response response = RestAssured.given()
                .when()
                .get("/coupons");

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].couponId", equalTo(1))
                .body("[0].couponName", equalTo("쿠폰"));
    }

    @DisplayName("쿠폰을 단건 조회한다.")
    @Test
    void find_coupon() {
        // given
        long couponId = couponService.createCoupon(new CouponCreateRequest("쿠폰", true, 10));

        // when & then
        Response response = RestAssured.given()
                .when()
                .get("/coupons/" + couponId);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("couponId", equalTo(1))
                .body("couponName", equalTo("쿠폰"));
    }

    @DisplayName("쿠폰을 저장한다.")
    @Test
    void create_coupon() {
        // given
        CouponCreateRequest request = new CouponCreateRequest("쿠폰", true, 10);

        // when & then
        Response response = RestAssured.given()
                .when()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/coupons");

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .header("location", "/coupons/1");
    }

    @DisplayName("쿠폰을 삭제한다.")
    @Test
    void delete_coupon_by_id() {
        // given
        long couponId = couponService.createCoupon(new CouponCreateRequest("쿠폰", true, 10));

        // when & then
        Response response = RestAssured.given()
                .when()
                .delete("/coupons/" + couponId);

        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("유저에게 쿠폰을 준다.")
    @Test
    void give_coupon_to_member() {
        // given
        long couponId = couponService.createCoupon(new CouponCreateRequest("쿠폰", true, 10));
        long memberId = 1;

        // when & then
        Response response = RestAssured.given()
                .when()
                .post("/coupons/" + couponId + "/members/" + memberId);

        response.then()
                .statusCode(HttpStatus.OK.value());
    }
}
