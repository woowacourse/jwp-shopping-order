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
import cart.order.application.dto.SpecificOrderResponse;
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
    final BigDecimal totalPrice = BigDecimal.valueOf(380400);
    final BigDecimal deliveryFee = BigDecimal.valueOf(3000);
    final long couponId = 1L;

    final RegisterOrderRequest registerOrderRequest = new RegisterOrderRequest(
        cartItemIdList,
        totalPrice,
        deliveryFee,
        couponId
    );

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
  @DisplayName("registerOrder() : 쿠폰을 사용하지 않고 주문이 성공적으로 생성된다면 Location에 저장된 리소스의 위치와 함께 201 Created를 반환한다.")
  void test_registerOrder_notCoupon() throws Exception {
    //given
    final Member member = memberDao.getMemberById(1L);

    final List<Long> cartItemIdList = List.of(1L, 2L);
    final BigDecimal totalPrice = BigDecimal.valueOf(380400);
    final BigDecimal deliveryFee = BigDecimal.valueOf(3000);
    final Long couponId = null;

    final RegisterOrderRequest registerOrderRequest = new RegisterOrderRequest(
        cartItemIdList,
        totalPrice,
        deliveryFee,
        couponId
    );

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
    List<OrderResponse> orderResponses =
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
        () -> assertEquals(3, orderResponses.size()),
        () -> assertEquals(3, orderResponses.get(0).getProducts().size()),
        () -> assertEquals(2, orderResponses.get(1).getProducts().size()),
        () -> assertEquals(1, orderResponses.get(2).getProducts().size())
    );
  }

  @Test
  @DisplayName("showOrder() : 사용자가 주문한 특정 주문 목록을 성공적으로 조회한다면 200 OK 를 반환한다.")
  void test_showOrder() throws Exception {
    //given
    final Member member = memberDao.getMemberById(1L);
    final long orderId = 1L;

    //when
    final SpecificOrderResponse specificOrderResponse = given().log().all()
        .auth().preemptive().basic(member.getEmail(), member.getPassword())
        .when()
        .get("/orders/{orderId}", orderId)
        .then()
        .log().all()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .jsonPath()
        .getObject(".", SpecificOrderResponse.class);

    //then
    assertAll(
        () -> assertEquals(3, specificOrderResponse.getProducts().size()),
        () -> assertEquals(0,
            BigDecimal.valueOf(585400).compareTo(specificOrderResponse.getTotalPrice())),
        () -> assertEquals(orderId, specificOrderResponse.getOrderId())
    );
  }

  @Test
  @DisplayName("deleteOrder() : 주문을 성공적으로 삭제한다면 204 No Content를 반환한다.")
  void test_deleteOrder() throws Exception {
    //given
    final Member member = memberDao.getMemberById(1L);
    final long orderId = 1L;

    //when & then
    given().log().all()
        .auth().preemptive().basic(member.getEmail(), member.getPassword())
        .when()
        .delete("/orders/{orderId}", orderId)
        .then()
        .log().all()
        .statusCode(HttpStatus.NO_CONTENT.value());
  }

  @Test
  @DisplayName("updateOrderStatus() : 주문 상태를 성공적으로 변경시키면 200 OK를 반환한다.")
  void test_updateOrderStatus() throws Exception {
    //given
    final Member member = memberDao.getMemberById(1L);
    final long orderId = 3L;

    //when & then
    given().log().all()
        .auth().preemptive().basic(member.getEmail(), member.getPassword())
        .when()
        .patch("/orders/{orderId}", orderId)
        .then()
        .log().all()
        .statusCode(HttpStatus.OK.value());
  }
}
