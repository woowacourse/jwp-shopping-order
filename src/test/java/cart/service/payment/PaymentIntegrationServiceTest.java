package cart.service.payment;

import cart.domain.member.Member;
import cart.dto.coupon.CouponIdRequest;
import cart.dto.history.OrderHistory;
import cart.dto.payment.PaymentRequest;
import cart.dto.payment.PaymentResponse;
import cart.dto.payment.PaymentUsingCouponsResponse;
import cart.dto.product.ProductIdRequest;
import cart.repository.cart.CartRepository;
import cart.repository.coupon.CouponRepository;
import cart.repository.member.MemberRepository;
import cart.repository.order.OrderRepository;
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
class PaymentIntegrationServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void init() {
        RestAssured.port = this.port;
    }

    @DisplayName("결제 페이지를 조회한다.")
    @Test
    void find_payment_page() {
        // given
        Member member = memberRepository.findMemberById(1);
        member.initCoupons(createCoupons());

        // when
        PaymentResponse result = paymentService.findPaymentPage(member);

        // then
        assertThat(result.getProducts().size()).isEqualTo(3);
    }

    @DisplayName("쿠폰을 적용하고 가격을 조회한다.")
    @Test
    void apply_coupons() {
        // given
        Member member = memberRepository.findMemberById(1);
        member.initCoupons(createCoupons());

        // when
        PaymentUsingCouponsResponse result = paymentService.applyCoupons(member, List.of(1L));

        // then
        assertAll(
                () -> assertThat(result.getProducts().size()).isEqualTo(3),
                () -> assertThat(result.getProducts().get(0).getDiscountPrice()).isEqualTo(1000)
        );
    }

    @DisplayName("결제를 한다.")
    @Test
    void pay() {
        // given
        Member member = memberRepository.findMemberById(1);
        member.initCoupons(createCoupons());
        PaymentRequest req = new PaymentRequest(List.of(new ProductIdRequest(1L, 1)), List.of(new CouponIdRequest(1L)));

        // when
        paymentService.pay(member, req);

        List<OrderHistory> result = orderRepository.findAllByMemberId(member.getId());

        // then
        assertAll(
                () -> assertThat(result.get(0).getCoupons().size()).isEqualTo(1),
                () -> assertThat(result.get(0).getProducts().size()).isEqualTo(1)
        );
    }
}
