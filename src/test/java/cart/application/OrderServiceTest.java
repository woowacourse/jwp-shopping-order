package cart.application;

import cart.domain.cartItem.CartItem;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.domain.member.MemberCoupons;
import cart.domain.product.Product;
import cart.dto.coupon.DiscountRequest;
import cart.dto.coupon.MemberCouponRequest;
import cart.dto.order.OrderItemRequest;
import cart.dto.order.OrderItemsRequests;
import cart.dto.order.OrderProductRequest;
import cart.exception.MemberCouponNotFoundException;
import cart.exception.OrderCartMismatchException;
import cart.repository.CartItemRepository;
import cart.repository.MemberCouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private MemberCouponRepository memberCouponRepository;
    @InjectMocks
    private OrderService orderService;

    @Test
    void OrderCartMismatchExceptionTest() {
        OrderItemRequest request = new OrderItemRequest(
                1L
                , new OrderProductRequest(1L, "치킨", 10000, "https://chicken"),
                10,
                Collections.emptyList()
        );

        OrderItemsRequests orderItemsRequests = new OrderItemsRequests(3000, List.of(request));

        given(cartItemRepository.findByMemberId(anyLong())).willReturn(List.of(new CartItem(1L, 10, new Product(1L, "치킨", 10000, "https://chicken"), new Member(1L, "a@a.com", "1234"))));
        given(cartItemRepository.findByIds(anyList())).willReturn(List.of());
        assertThatThrownBy(() -> orderService.createOrder(orderItemsRequests, new Member(1L, "a@a.com", "1234")))
                .isInstanceOf(OrderCartMismatchException.class);
    }

    @Test
    void MemberCouponNotFoundExceptionTest() {
        OrderItemRequest request = new OrderItemRequest(
                3L,
                new OrderProductRequest(1L, "치킨", 10000, "https://chicken"),
                10,
                List.of(new MemberCouponRequest(2L, "쿠폰", new DiscountRequest("rate", 10)))
        );

        OrderItemsRequests orderItemsRequests = new OrderItemsRequests(3000, List.of(request));
        given(memberCouponRepository.findByIds(eq(List.of(2L)))).willReturn(new MemberCoupons(new ArrayList<>(List.of(new MemberCoupon(2L, new Coupon(7L, "쿠폰", new Discount("rate", 10)), false)))));
        given(memberCouponRepository.findByMemberId(eq(5L))).willReturn(new MemberCoupons(new ArrayList<>()));
        given(cartItemRepository.findByIds(eq(List.of(3L)))).willReturn(List.of(new CartItem(3L, 10, new Product(1L, "치킨", 10000, "https://chicken"), new Member(5L, "a@a.com", "1234"))));
        given(cartItemRepository.findByMemberId(eq(5L))).willReturn(List.of(new CartItem(3L, 10, new Product(1L, "치킨", 10000, "https://chicken"), new Member(5L, "a@a.com", "1234"))));

        assertThatThrownBy(() -> orderService.createOrder(orderItemsRequests, new Member(5L, "a@a.com", "1234")))
                .isInstanceOf(MemberCouponNotFoundException.class);
    }
}
