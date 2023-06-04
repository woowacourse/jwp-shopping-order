package cart.ui;

import static org.hamcrest.Matchers.hasSize;

import cart.application.CouponService;
import cart.application.MemberService;
import cart.domain.Member;
import cart.dto.SaveCouponRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CouponApiControllerTest {

  private static final String HEADER = "Authorization";
  private static final String TYPE = "Basic ";
  private static final String EMAIL = "a@a.com";
  private static final String DELIMITER = ":";
  private static final String PASSWORD = "1234";

  @LocalServerPort
  private int port;

  @Autowired
  private CouponService couponService;

  @Autowired
  private MemberService memberService;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

  @Test
  void findMemberCoupons() {
    final Long coupon1 = couponService.createCoupon(new SaveCouponRequest("1000원 할인", 10000, 1000));
    final Long coupon2 = couponService.createCoupon(new SaveCouponRequest("2000원 할인", 20000, 2000));
    final Member member = memberService.findByEmail(EMAIL);
    couponService.issueCoupon(member, coupon1);
    couponService.issueCoupon(member, coupon2);

    RestAssured
        .given()
        .header(HEADER, TYPE + Base64Coder.encodeString(EMAIL + DELIMITER + PASSWORD))
        .when()
        .get("/coupons")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("couponResponse", hasSize(2));
  }

  @Test
  void findAvailableCoupons() {
    final Long coupon1 = couponService.createCoupon(new SaveCouponRequest("1000원 할인", 10000, 1000));
    final Long coupon2 = couponService.createCoupon(new SaveCouponRequest("2000원 할인", 20000, 2000));
    final Member member = memberService.findByEmail(EMAIL);
    couponService.issueCoupon(member, coupon1);
    couponService.issueCoupon(member, coupon2);

    RestAssured
        .given()
        .header(HEADER, TYPE + Base64Coder.encodeString(EMAIL + DELIMITER + PASSWORD))
        .queryParam("total", 15000)
        .when()
        .get("/coupons/active")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("couponResponse", hasSize(1));
  }

  @Test
  void calculateDiscountAmount() {
    final Long couponId = couponService.createCoupon(new SaveCouponRequest("1000원 할인", 10000, 1000));
    final Member member = memberService.findByEmail(EMAIL);
    couponService.issueCoupon(member, couponId);

    RestAssured
        .given()
        .header(HEADER, TYPE + Base64Coder.encodeString(EMAIL + DELIMITER + PASSWORD))
        .queryParam("total", 15000)
        .when()
        .get("/coupons/" + couponId + "/discount")
        .then()
        .statusCode(HttpStatus.OK.value());
  }
}
