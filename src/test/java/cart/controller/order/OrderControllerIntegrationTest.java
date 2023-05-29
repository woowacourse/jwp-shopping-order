package cart.controller.order;

import cart.domain.member.Member;
import cart.dto.coupon.CouponIdRequest;
import cart.dto.payment.PaymentRequest;
import cart.dto.product.ProductIdRequest;
import cart.repository.member.MemberRepository;
import cart.service.order.OrderService;
import cart.service.payment.PaymentService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/setData.sql")
class OrderControllerIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PaymentService paymentService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @DisplayName("주문 내역을 모두 반환한다.")
    @Test
    void find_orders() {
        Member member = memberRepository.findMemberById(1);
        PaymentRequest req = new PaymentRequest(List.of(new ProductIdRequest(1L, 1)), List.of(new CouponIdRequest(1L)));
        long orderId = paymentService.pay(member, req);

        // when & then
        Response response = RestAssured.given()
                .auth().preemptive().basic("a@a.com", "1234")
                .when().get("/orders");

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("orders[0].orderId", equalTo(1))
                .body("orders[0].products[0].productId", equalTo(1))
                .body("orders[0].products[0].productName", equalTo("치킨"))
                .body("orders[0].products[0].imgUrl", equalTo("https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"))
                .body("orders[0].products[0].quantity", equalTo(1))
                .body("orders[0].products[0].price", equalTo(9000))
                .body("orders[0].deliveryPrice.deliveryPrice", equalTo(3000))
                .body("orders[0].coupons[0].couponId", equalTo(1))
                .body("orders[0].coupons[0].couponName", equalTo("전체 10% 할인 쿠폰"));
    }

    @DisplayName("주문 내역을 한 개 반환한다.")
    @Test
    void find_order() {
        Member member = memberRepository.findMemberById(1);
        PaymentRequest req = new PaymentRequest(List.of(new ProductIdRequest(1L, 1)), List.of(new CouponIdRequest(1L)));
        long orderId = paymentService.pay(member, req);

        // when & then
        Response response = RestAssured.given()
                .auth().preemptive().basic("a@a.com", "1234")
                .when().get("/orders/" + orderId);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("orderId", equalTo(1))
                .body("products[0].productId", equalTo(1))
                .body("products[0].productName", equalTo("치킨"))
                .body("products[0].imgUrl", equalTo("https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"))
                .body("products[0].quantity", equalTo(1))
                .body("products[0].price", equalTo(9000))
                .body("deliveryPrice.deliveryPrice", equalTo(3000))
                .body("coupons[0].couponId", equalTo(1))
                .body("coupons[0].couponName", equalTo("전체 10% 할인 쿠폰"));
    }
}
