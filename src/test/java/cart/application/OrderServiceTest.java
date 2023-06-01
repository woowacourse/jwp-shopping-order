package cart.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.dto.request.OrderRequest;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderDao orderDao;
    @Mock
    OrderItemDao orderItemDao;
    @Mock
    CartItemDao cartItemDao;
    @Mock
    CouponDao couponDao;

    @InjectMocks
    OrderService orderService;

    @Test
    void add() {
        // 준비
        Member member = new Member(1L, "aa@aa.aa", "aa", "aa");
        List<Long> cartItemIds = List.of(1L, 2L);
        OrderRequest orderRequest = new OrderRequest(cartItemIds, 1L);

        List<CartItem> cartItems = List.of(
            new CartItem(1L, 3, new Product(1L, "치킨", 10_000, "http://example.com/chicken.jpg"), member),
            new CartItem(2L, 4, new Product(2L, "피자", 20_000, "http://example.com/pizza.jpg"), member)
        );
        Coupon coupon = new Coupon(1L, "할인쿠폰", 100, CouponType.FIXED_PERCENTAGE, null, 0.1, 10000);

        when(cartItemDao.findByIds(cartItemIds)).thenReturn(cartItems);
        when(couponDao.findById(anyLong())).thenReturn(coupon);
        when(orderDao.addOrder(any(Order.class))).thenReturn(1L);

        Long orderId = orderService.add(member, orderRequest);

        assertNotNull(orderId);
        verify(orderItemDao).saveAllItems(eq(1L), any(Order.class));
        verify(cartItemDao).deleteByIds(cartItemIds);
    }
}
