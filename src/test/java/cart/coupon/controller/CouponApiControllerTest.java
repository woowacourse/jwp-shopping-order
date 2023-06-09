package cart.coupon.controller;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cart.coupon.application.dto.CouponResponse;
import cart.helper.IntegrationTestHelper;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class CouponApiControllerTest extends IntegrationTestHelper {

  @Autowired
  private MemberDao memberDao;

  @Test
  @DisplayName("showCoupons() : 해당 member 가 가지고 있는 쿠폰들을 성공적으로 조회할 수 있다면 200 OK 를 반환한다.")
  void test_showCoupons() throws Exception {
    //given
    final Member member = memberDao.getMemberById(1L);

    //when
    final List<CouponResponse> couponResponses = given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .auth().preemptive().basic(member.getEmail(), member.getPassword())
        .when()
        .get("/coupons")
        .then().log().all()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .jsonPath()
        .getList(".", CouponResponse.class);

    //then
    assertEquals(2, couponResponses.size());
  }
}
