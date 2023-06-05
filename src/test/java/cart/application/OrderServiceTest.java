package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberCouponDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.domain.Money;
import cart.domain.Product;
import cart.dto.response.MemberCouponResponse;
import cart.dto.response.MemberCouponsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderItemDao orderItemDao;
    @Mock
    private CartItemDao cartItemDao;
    @Mock
    private MemberCouponDao memberCouponDao;

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderDao, orderItemDao, cartItemDao, memberCouponDao);
    }

    @Test
    public void testGetMemberCoupons() {
        Member member = new Member(1L, "a@a.com", "1234", "라잇");
        Product product1 = new Product(1L, "1", 1000, "http://www.aa.aa");
        Product product2 = new Product(1L, "1", 1000, "http://www.aa.aa");
        Coupon coupon1 = new Coupon(1L, "10프로 할인", 1000, CouponType.FIXED_PERCENTAGE, null, 0.1, 10000);
        Coupon coupon2 = new Coupon(1L, "5000원 할인", 5000, CouponType.FIXED_AMOUNT, 5000, null, null);

        List<Long> cartItemIds = Arrays.asList(1L, 2L);
        List<CartItem> cartItems = Arrays.asList(
            new CartItem(1L, 2, product1, member),
            new CartItem(2L, 4, product2, member)
        );

        Money price = new Money(10000);

        List<MemberCoupon> memberCoupons = Arrays.asList(
            new MemberCoupon(1L, member, coupon1, new Timestamp(2023, 6, 30, 0, 0, 0, 0)),
            new MemberCoupon(1L, member, coupon2, new Timestamp(2023, 6, 30, 0, 0, 0, 0))
        );

        when(cartItemDao.findByIds(cartItemIds)).thenReturn(cartItems);
        when(memberCouponDao.findByMemberId(member.getId())).thenReturn(memberCoupons);

        MemberCouponsResponse expectedResponse = new MemberCouponsResponse(Arrays.asList(
            MemberCouponResponse.of(memberCoupons.get(0), price),
            MemberCouponResponse.of(memberCoupons.get(1), price)
        ));

        MemberCouponsResponse actualResponse = orderService.findMemberCoupons(member, cartItemIds);

        assertEquals(expectedResponse.getCoupons().get(0).getId(), actualResponse.getCoupons().get(0).getId());
        assertEquals(expectedResponse.getCoupons().get(1).getId(), actualResponse.getCoupons().get(1).getId());

        verify(cartItemDao, times(1)).findByIds(cartItemIds);
        verify(memberCouponDao, times(1)).findByMemberId(member.getId());
    }
}

