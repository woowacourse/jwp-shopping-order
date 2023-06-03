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
  void showCartItems() {
    final Long coupon1 = couponService.createCoupon(new SaveCouponRequest("1000원 할인", 10000, 1000));
    final Long coupon2 = couponService.createCoupon(new SaveCouponRequest("2000원 할인", 20000, 2000));
    final String email = "a@a.com";
    final Member member = memberService.findByEmail(email);
    couponService.issueCoupon(member, coupon1);
    couponService.issueCoupon(member, coupon2);

    RestAssured
        .given()
        .header("Authorization", "Basic " + Base64Coder.encodeString(email + ":" + "1234"))
        .when()
        .get("/coupons")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("couponResponse", hasSize(2));
  }
}
