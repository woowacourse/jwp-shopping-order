package cart.integration;

import static cart.exception.ErrorCode.COUPON_DUPLICATE;
import static cart.exception.ErrorCode.COUPON_NOT_FOUND;
import static cart.exception.ErrorCode.INVALID_REQUEST;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import cart.application.dto.coupon.CouponRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class CouponIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("쿠폰 리스트를 조회한다.")
    public void getCoupons() {
        // given
        /** 쿠폰 저장 */
        final CouponRequest 신규_가입_쿠폰_등록_요청 = new CouponRequest("신규 가입 축하 쿠폰", 20, 365);
        final CouponRequest 첫_주문_쿠폰_등록_요청 = new CouponRequest("첫 주문 감사 쿠폰", 10, 10);
        쿠폰_저장(신규_가입_쿠폰_등록_요청);
        쿠폰_저장(첫_주문_쿠폰_등록_요청);

        // expected
        given().log().all()
            .when()
            .get("/coupons")
            .then()
            .body("size", is(2))
            .body("[0].id", equalTo(1))
            .body("[0].name", equalTo("신규 가입 축하 쿠폰"))
            .body("[0].discountRate", equalTo(20))
            .body("[1].id", equalTo(2))
            .body("[1].name", equalTo("첫 주문 감사 쿠폰"))
            .body("[1].discountRate", equalTo(10));
    }

    @Test
    @DisplayName("쿠폰을 추가한다.")
    public void createCoupon() {
        // given
        final CouponRequest 신규_가입_쿠폰_등록_요청 = new CouponRequest("신규 가입 축하 쿠폰", 20, 365);

        // expected
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(신규_가입_쿠폰_등록_요청)
            .when()
            .post("/coupons")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .header(LOCATION, "/coupons/" + 1);
    }

    @Test
    @DisplayName("쿠폰 추가 시 비어있는 정보로 요청이 들어오면 예외가 발생한다.")
    public void createCoupon_invalid_request() {
        // given
        final CouponRequest 잘못된_신규_가입_쿠폰_등록_요청 = new CouponRequest("", null, null);

        // expected
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(잘못된_신규_가입_쿠폰_등록_요청)
            .when()
            .post("/coupons")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode", equalTo(INVALID_REQUEST.name()))
            .body("errorMessage", containsInAnyOrder("쿠폰 이름은 비어있을 수 없습니다.",
                "쿠폰 할인율은 비어있을 수 없습니다.", "쿠폰 기간은 비어있을 수 없습니다."));
    }

    @Test
    @DisplayName("쿠폰 추가 시 이미 존재하는 이름, 할인율을 가진 쿠폰 정보가 들어온다면 예외가 발생한다.")
    public void createCoupon_duplicated_request() {
        // given
        /** 쿠폰 저장 */
        final CouponRequest 신규_가입_쿠폰_등록_요청 = new CouponRequest("신규 가입 축하 쿠폰", 20, 365);
        쿠폰_저장(신규_가입_쿠폰_등록_요청);

        final CouponRequest 중복된_신규_가입_쿠폰_등록_요청 = new CouponRequest("신규 가입 축하 쿠폰", 20, 10);

        // expected
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(중복된_신규_가입_쿠폰_등록_요청)
            .when()
            .post("/coupons")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode", equalTo(COUPON_DUPLICATE.name()))
            .body("errorMessage", equalTo("이미 존재하는 쿠폰 정보입니다."));
    }

    @Test
    @DisplayName("특정 쿠폰을 조회한다.")
    public void getCreatedCoupon() {
        // given
        /** 쿠폰 저장 */
        final CouponRequest 신규_가입_쿠폰_등록_요청 = new CouponRequest("신규 가입 축하 쿠폰", 20, 365);
        쿠폰_저장(신규_가입_쿠폰_등록_요청);

        // expected
        given().log().all()
            .when()
            .get("/coupons/{id}", 1)
            .then()
            .body("name", equalTo("신규 가입 축하 쿠폰"))
            .body("discountRate", equalTo(20));
    }

    @Test
    @DisplayName("쿠폰 정보를 삭제한다.")
    void deleteCoupon() {
        // given
        final CouponRequest 신규_가입_쿠폰_등록_요청 = new CouponRequest("신규 가입 축하 쿠폰", 20, 365);
        쿠폰_저장(신규_가입_쿠폰_등록_요청);

        // when
        given().log().all()
            .when()
            .delete("/coupons/{id}", 1)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        given().log().all()
            .when()
            .get("/coupons/1")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("errorCode", equalTo(COUPON_NOT_FOUND.name()))
            .body("errorMessage", equalTo("쿠폰 정보를 찾을 수 없습니다."));
    }
}
