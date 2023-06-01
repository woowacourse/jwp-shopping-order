package cart.application;

import cart.dao.CartItemDao;
import cart.domain.cartItem.CartItem;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.domain.member.MemberCoupons;
import cart.domain.product.Product;
import cart.dto.order.OrderProductRequest;
import cart.dto.order.OrderRequest;
import cart.exception.MemberCouponNotFoundException;
import cart.exception.OrderCartMismatchException;
import cart.repository.MemberCouponRepository;
import cart.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CartItemDao cartItemDao;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MemberCouponRepository memberCouponRepository;
    @InjectMocks
    private OrderService orderService;

    @Test
    void OrderCartMismatchExceptionTest() {
        OrderRequest request = new OrderRequest(
                new OrderProductRequest(1L, "치킨", 11000, "https://chicken"),
                10,
                Collections.emptyList()
        );

        given(cartItemDao.findByMemberId(anyLong())).willReturn(List.of(new CartItem(1L, 10, new Product(1L, "치킨", 10000, "https://chicken"), new Member(1L, "a@a.com", "1234"))));
        assertThatThrownBy(() -> orderService.createOrder(List.of(request), new Member(1L, "a@a.com", "1234")))
                .isInstanceOf(OrderCartMismatchException.class);
    }

    @Test
    void MemberCouponNotFoundExceptionTest() {
        OrderRequest request = new OrderRequest(
                new OrderProductRequest(1L, "치킨", 10000, "https://chicken"),
                10,
                Collections.emptyList()
        );


        given(memberCouponRepository.findByIds(anyList())).willReturn(new MemberCoupons(new ArrayList<>(List.of(new MemberCoupon(1L, new Coupon(2L, "쿠폰", new Discount("rate", 10)), false)))));
        given(memberCouponRepository.findByMemberId(anyLong())).willReturn(new MemberCoupons(new ArrayList<>(List.of(new MemberCoupon(2L, new Coupon(2L, "쿠폰", new Discount("rate", 10)), false)))));
        given(cartItemDao.findByMemberId(anyLong())).willReturn(List.of(new CartItem(1L, 10, new Product(1L, "치킨", 10000, "https://chicken"), new Member(1L, "a@a.com", "1234"))));

        assertThatThrownBy(() -> orderService.createOrder(List.of(request), new Member(1L, "a@a.com", "1234")))
                .isInstanceOf(MemberCouponNotFoundException.class);
    }

    @Test
    void orderServiceCreateTest() {
        // given
        OrderRequest request = new OrderRequest(
                new OrderProductRequest(1L, "치킨", 10000, "https://chicken"),
                10,
                List.of(1L)
        );

        given(memberCouponRepository.findByIds(anyList())).willReturn(new MemberCoupons(new ArrayList<>(List.of(new MemberCoupon(1L, new Coupon(2L, "쿠폰", new Discount("rate", 10)), false)))));
        given(memberCouponRepository.findByMemberId(anyLong())).willReturn(new MemberCoupons(new ArrayList<>(List.of(new MemberCoupon(1L, new Coupon(2L, "쿠폰", new Discount("rate", 10)), false)))));
        given(cartItemDao.findByMemberId(anyLong())).willReturn(List.of(new CartItem(1L, 10, new Product(1L, "치킨", 10000, "https://chicken"), new Member(1L, "a@a.com", "1234"))));

        // when
        orderService.createOrder(List.of(request), new Member(1L, "a@a.com", "1234"));

        // then
        Assertions.assertAll(
                () -> verify(memberCouponRepository).updateCoupons(any(MemberCoupons.class), any(Member.class)),
                () -> verify(cartItemDao).delete(anyLong(), anyList()),
                () -> verify(orderRepository).create(anyList(), anyLong())
        );
    }
}
