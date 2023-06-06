package cart.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.domain.Money;
import cart.domain.Product;
import cart.dto.request.MemberCouponAddRequest;
import cart.dto.response.CouponResponse;
import cart.dto.response.CouponsResponse;
import cart.dto.response.MemberCouponResponse;
import cart.dto.response.MemberCouponsResponse;
import cart.exception.BadRequestException;

class CouponServiceTest {

    @Mock
    private CartItemDao cartItemDao;
    @Mock
    private MemberCouponDao memberCouponDao;
    @Mock
    private CouponDao couponDao;

    private CouponService couponService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        couponService = new CouponService(cartItemDao, couponDao, memberCouponDao);
    }

    @Test
    @DisplayName("멤버와 장바구니 물품들로 멤버에게 발급된 쿠폰을 조회한다.")
    public void testFindMemberCoupons() {
        Member member = new Member(1L, "a@a.com", "1234", "라잇");
        Product product1 = new Product(1L, "1", 1000, "http://www.aa.aa");
        Product product2 = new Product(2L, "2", 2000, "http://www.aa.aa");
        Coupon coupon1 = new Coupon(1L, "10프로 할인", 1000, CouponType.FIXED_PERCENTAGE, null, 0.1, 10000);
        Coupon coupon2 = new Coupon(2L, "5000원 할인", 5000, CouponType.FIXED_AMOUNT, 5000, null, null);

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

        MemberCouponsResponse actualResponse = couponService.findMemberCoupons(member, cartItemIds);

        assertEquals(expectedResponse.getCoupons().get(0).getId(), actualResponse.getCoupons().get(0).getId());
        assertEquals(expectedResponse.getCoupons().get(1).getId(), actualResponse.getCoupons().get(1).getId());

        verify(cartItemDao, times(1)).findByIds(cartItemIds);
        verify(memberCouponDao, times(1)).findByMemberId(member.getId());
    }

    @Test
    @DisplayName("모든 쿠폰을 조회한다.")
    public void testFindAllCoupons() {
        Coupon coupon1 = new Coupon(1L, "10프로 할인", 1000, CouponType.FIXED_PERCENTAGE, null, 0.1, 10000);
        Coupon coupon2 = new Coupon(2L, "5000원 할인", 5000, CouponType.FIXED_AMOUNT, 5000, null, null);

        when(couponDao.findAll()).thenReturn(Arrays.asList(coupon1, coupon2));

        CouponsResponse expectedResponse = new CouponsResponse(Arrays.asList(
            CouponResponse.of(coupon1),
            CouponResponse.of(coupon2)
        ));

        CouponsResponse actualResponse = couponService.findAllCoupons();

        assertEquals(expectedResponse.getCoupons().get(0).getId(), actualResponse.getCoupons().get(0).getId());
        assertEquals(expectedResponse.getCoupons().get(1).getId(), actualResponse.getCoupons().get(1).getId());

        verify(couponDao, times(1)).findAll();
    }

    @Test
    @DisplayName("멤버에게 쿠폰을 추가한다.")
    public void testAddMemberCoupon() {
        Member member = new Member(1L, "a@a.com", "1234", "라잇");
        Coupon coupon = new Coupon(1L, "10프로 할인", 1000, CouponType.FIXED_PERCENTAGE, null, 0.1, 10000);
        MemberCouponAddRequest request = new MemberCouponAddRequest(LocalDateTime.of(2023, 6, 30, 0, 0));

        when(couponDao.findById(1L)).thenReturn(coupon);
        when(memberCouponDao.save(any(MemberCoupon.class))).thenReturn(1L);

        Long actualCouponId = couponService.addMemberCoupon(member, 1L, request);

        assertEquals(1L, actualCouponId);

        verify(couponDao, times(1)).findById(1L);
        verify(memberCouponDao, times(1)).save(any(MemberCoupon.class));
    }

    @Test
    @DisplayName("추가할 쿠폰이 존재하지 않을 때 BadRequestException을 던진다.")
    public void testAddMemberCouponWithNonExistentCoupon() {
        Member member = new Member(1L, "a@a.com", "1234", "라잇");
        MemberCouponAddRequest request = new MemberCouponAddRequest(LocalDateTime.of(2023, 6, 30, 0, 0));

        when(couponDao.findById(1L)).thenReturn(null);

        assertThrows(BadRequestException.class, () -> couponService.addMemberCoupon(member, 1L, request));

        verify(couponDao, times(1)).findById(1L);
        verify(memberCouponDao, never()).save(any(MemberCoupon.class));
    }
}
