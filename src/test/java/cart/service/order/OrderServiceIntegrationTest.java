package cart.service.order;

import cart.domain.member.Member;
import cart.dto.coupon.CouponIdRequest;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrdersResponse;
import cart.dto.payment.PaymentRequest;
import cart.dto.product.ProductIdRequest;
import cart.repository.member.MemberRepository;
import cart.repository.order.OrderRepository;
import cart.service.payment.PaymentService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static cart.fixture.CouponFixture.createCoupons;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/setData.sql")
public class OrderServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PaymentService paymentService;

    @BeforeEach
    void init() {
        RestAssured.port = this.port;
    }

    @DisplayName("주문 내역들을 보여준다.")
    @Test
    void find_orders() {
        // given
        Member member = memberRepository.findMemberById(1);
        member.initCoupons(createCoupons());
        PaymentRequest req = new PaymentRequest(List.of(new ProductIdRequest(1L, 1)), List.of(new CouponIdRequest(1L)));

        // when
        long orderId = paymentService.pay(member, req);
        OrdersResponse orders = orderService.findOrders(member);

        // then
        assertAll(
                () -> assertThat(orders.getOrders().size()).isEqualTo(1),
                () -> assertThat(orders.getOrders().get(0).getProducts().get(0).getProductName()).isEqualTo("치킨")
        );
    }

    @DisplayName("주문 내역을 보여준다.")
    @Test
    void find_order() {
        // given
        Member member = memberRepository.findMemberById(1);
        member.initCoupons(createCoupons());
        PaymentRequest req = new PaymentRequest(List.of(new ProductIdRequest(1L, 1)), List.of(new CouponIdRequest(1L)));

        // when
        long orderId = paymentService.pay(member, req);
        OrderResponse order = orderService.findOrder(member, orderId);

        // then
        assertAll(
                () -> assertThat(order.getOrderId()).isEqualTo(orderId),
                () -> assertThat(order.getProducts().get(0).getProductName()).isEqualTo("치킨")
        );
    }
}
