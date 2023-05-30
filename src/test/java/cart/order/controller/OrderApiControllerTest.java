package cart.order.controller;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import cart.helper.IntegrationTestHelper;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.order.application.dto.OrderResponse;
import cart.order.application.dto.RegisterOrderRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class OrderApiControllerTest extends IntegrationTestHelper {

  @Autowired
  private MemberDao memberDao;

  @Test
  @DisplayName("registerOrder() : 주문이 성공적으로 생성된다면 Location에 저장된 리소스의 위치와 함께 201 Created를 반환한다.")
  void test_registerOrder() throws Exception {
    //given
    final Member member = memberDao.getMemberById(1L);

    final List<Long> cartItemIdList = List.of(1L, 2L);
    final BigDecimal totalPrice = BigDecimal.valueOf(10000);
    final BigDecimal deliveryFee = BigDecimal.valueOf(3000);

    final RegisterOrderRequest registerOrderRequest = new RegisterOrderRequest(cartItemIdList,
        totalPrice, deliveryFee);

    //when
    final String location = given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .auth().preemptive().basic(member.getEmail(), member.getPassword())
        .body(registerOrderRequest)
        .when()
        .post("/orders")
        .then()
        .log().all()
        .statusCode(HttpStatus.CREATED.value())
        .extract().header("Location");

    //then
    assertNotNull(location);
  }

  @Test
  @DisplayName("showOrders() : 사용자가 주문한 주문 목록들을 성공적으로 조회한다면 200 OK 를 반환한다.")
  void test_showOrders() throws Exception {
    //given
    final Member member = memberDao.getMemberById(1L);

    //when
    List<OrderResponse> orderRespons =
        given().log().all()
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .get("/orders")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", OrderResponse.class);

    //then
    assertAll(
        () -> assertEquals(1, orderRespons.size()),
        () -> assertEquals(3, orderRespons.get(0).getProducts().size())
    );
  }
}
