package cart.service.payment;

import cart.domain.cart.Cart;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
import cart.domain.coupon.MemberCoupons;
import cart.domain.discount.PolicyDiscount;
import cart.domain.member.Member;
import cart.dto.coupon.CouponIdRequest;
import cart.dto.order.OrderResponse;
import cart.dto.payment.PaymentRequest;
import cart.dto.payment.PaymentResponse;
import cart.dto.payment.PaymentUsingCouponsResponse;
import cart.dto.product.ProductIdRequest;
import cart.repository.cart.CartRepository;
import cart.repository.coupon.CouponRepository;
import cart.repository.order.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static cart.fixture.CartFixture.createCart;
import static cart.fixture.CouponFixture.createCoupons;
import static cart.fixture.MemberFixture.createMember;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceUnitTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private OrderRepository orderRepository;

    @DisplayName("구매 페이지를 조회한다.")
    @Test
    void find_payment_page() {
        // given
        Member member = createMember();
        MemberCoupons memberCoupons = new MemberCoupons(member, createCoupons());
        Cart cart = createCart();

        given(cartRepository.findCartByMemberId(member.getId())).willReturn(cart);

        // when
        PaymentResponse result = paymentService.findPaymentPage(memberCoupons);

        // then
        assertAll(
                () -> assertThat(result.getProducts().size()).isEqualTo(2),
                () -> assertThat(result.getCoupons().size()).isEqualTo(2)
        );
    }

    @DisplayName("쿠폰을 적용하면, 적용 후 가격이 나온다.")
    @Test
    void apply_coupon_and_returns_price() {
        // given
        Member member = createMember();
        Coupons reqCoupons = new Coupons(List.of(new Coupon(1L, "쿠폰", new PolicyDiscount(1000))));
        MemberCoupons memberCoupons = new MemberCoupons(member, createCoupons());
        Cart cart = createCart();

        given(cartRepository.findCartByMemberId(member.getId())).willReturn(cart);
        given(couponRepository.findAllByCouponIds(List.of(1L))).willReturn(reqCoupons);

        // when
        PaymentUsingCouponsResponse result = paymentService.applyCoupons(memberCoupons, List.of(1L));

        // then
        assertAll(
                () -> assertThat(result.getProducts().size()).isEqualTo(2),
                () -> assertThat(result.getDeliveryPrice().getDiscountPrice()).isEqualTo(0)
        );
    }

    @DisplayName("결제를 한다.")
    @Test
    void pay() {
        // given
        Member member = createMember();
        PaymentRequest req = new PaymentRequest(List.of(new ProductIdRequest(1L, 1)), List.of(new CouponIdRequest(1L)));
        Cart cart = createCart();
        MemberCoupons memberCoupons = new MemberCoupons(member, createCoupons());

        given(cartRepository.findCartByMemberId(member.getId())).willReturn(cart);

        // when
        paymentService.pay(memberCoupons, req);

        // then
        verify(orderRepository).save(any(), any(OrderResponse.class));
    }
}
