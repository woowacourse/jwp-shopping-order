package cart.application;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.domain.cartItem.CartItem;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.order.OrderProductRequest;
import cart.dto.order.OrderRequest;
import cart.exception.MemberCouponNotFoundException;
import cart.exception.OrderCartMismatchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CouponDao couponDao;
    @Mock
    private CartItemDao cartItemDao;
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

        given(couponDao.findById(anyList())).willReturn(List.of(new Coupon(1L, "쿠폰", new Discount("rate", 10))));

        assertThatThrownBy(() -> orderService.createOrder(List.of(request), new Member(1L, "a@a.com", "1234").setCoupons(List.of(new Coupon(2L, "쿠폰", new Discount("rate", 10))))))
                .isInstanceOf(MemberCouponNotFoundException.class);
    }
}
