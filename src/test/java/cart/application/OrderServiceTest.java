package cart.application;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.OrderBaseCouponGenerator;
import cart.domain.order.Order;
import cart.dto.order.OrderDetailResponse;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.fixtures.OrderFixtures;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static cart.fixtures.CartItemFixtures.CART_ITEM1;
import static cart.fixtures.CartItemFixtures.CART_ITEM2;
import static cart.fixtures.CouponFixtures.BONUS_COUPON;
import static cart.fixtures.CouponFixtures.COUPON3;
import static cart.fixtures.MemberFixtures.MEMBER1;
import static cart.fixtures.MemberFixtures.MEMBER1_NULL_PASSWORD;
import static cart.fixtures.OrderFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;
    @Mock
    CartItemRepository cartItemRepository;
    @Mock
    CouponRepository couponRepository;
    @Mock
    OrderBaseCouponGenerator orderBaseCouponGenerator;

    @Nested
    @DisplayName("주문을 한다.")
    class addOrder {

        @Test
        @DisplayName("주문에 쿠폰을 적용했다면 쿠폰을 사용하고, 주문 금액이 5만원 미만이면 추가 쿠폰 발행은 없다.")
        void useCouponTest() {
            // given
            Member member = MEMBER1_NULL_PASSWORD;
            OrderRequest request = OrderFixtures.ORDER_REQUEST_WITH_COUPON;
            Order orderToAdd = ORDER_WITH_COUPON_TO_INSERT;
            Order order = ORDER_WITH_COUPON;
            Coupon usedCoupon = COUPON3;
            when(cartItemRepository.findById(CART_ITEM1.getId())).thenReturn(CART_ITEM1);
            when(cartItemRepository.findById(CART_ITEM2.getId())).thenReturn(CART_ITEM2);
            when(couponRepository.findById(usedCoupon.getId())).thenReturn(usedCoupon);
            when(orderRepository.save(any(Order.class))).thenReturn(order.getId());
            doNothing().when(cartItemRepository).deleteById(anyLong());
            doNothing().when(couponRepository).deleteMemberCoupon(member.getId(), usedCoupon.getId());

            // when, then
            assertThatNoException().isThrownBy(() -> orderService.addOrder(member, request));
        }

        @Test
        @DisplayName("주문에 쿠폰을 적용했다면 쿠폰을 사용하고, 주문 금액이 5만원 미만이면 추가 쿠폰 발행은 없다.")
        void issueAdditionalCouponTest() {
            // given
            Member member = MEMBER1_NULL_PASSWORD;
            OrderRequest request = OrderFixtures.ORDER_REQUEST_WITH_COUPON;
            Order orderToAdd = ORDER_WITH_COUPON_TO_INSERT;
            Order order = ORDER_WITH_COUPON;
            Coupon usedCoupon = COUPON3;
            when(cartItemRepository.findById(CART_ITEM1.getId())).thenReturn(CART_ITEM1);
            when(cartItemRepository.findById(CART_ITEM2.getId())).thenReturn(CART_ITEM2);
            when(couponRepository.findById(usedCoupon.getId())).thenReturn(usedCoupon);
            when(orderRepository.save(any(Order.class))).thenReturn(order.getId());
            doNothing().when(cartItemRepository).deleteById(anyLong());
            doNothing().when(couponRepository).deleteMemberCoupon(member.getId(), usedCoupon.getId());
            when(orderBaseCouponGenerator.generate(any(Order.class))).thenReturn(Optional.of(BONUS_COUPON));

            // when, then
            assertThatNoException().isThrownBy(() -> orderService.addOrder(member, request));
        }
    }

    @Test
    @DisplayName("회원의 주문 목록을 가져온다.")
    void findByMemberTest() {
        // given
        Member member = MEMBER1;
        when(orderRepository.findByMemberId(member.getId())).thenReturn(List.of(ORDER1, ORDER2));
        List<OrderResponse> expectResponse = List.of(ORDER_RESPONSE1, ORDER_RESPONSE2);

        // when
        List<OrderResponse> findResponse = orderService.findByMember(member);

        // then
        assertThat(findResponse).isEqualTo(expectResponse);
    }

    @Test
    @DisplayName("ID에 해당하는 주문 상세 목록을 가져온다.")
    void findByIDTest() {
        // given
        OrderDetailResponse expectResponse = ORDER_DETAIL_RESPONSE;
        Order order = ORDER1;
        Long orderId = order.getId();
        when(orderRepository.findById(orderId)).thenReturn(order);

        // when
        OrderDetailResponse findResponse = orderService.findById(orderId);

        // then
        assertThat(findResponse).isEqualTo(expectResponse);
    }
}