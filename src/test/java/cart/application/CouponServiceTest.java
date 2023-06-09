package cart.application;

import cart.application.CouponService;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItems;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.domain.member.Member;
import cart.dto.response.AllCouponResponse;
import cart.dto.response.AllOrderCouponResponse;
import cart.dto.request.MemberCouponRequest;
import cart.fixture.CartItemFixture;
import cart.fixture.CouponFixture;
import cart.fixture.MemberFixture;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cart.fixture.CartItemFixture.장바구니1;
import static cart.fixture.CartItemFixture.장바구니2;
import static cart.fixture.CouponFixture.테스트쿠폰1;
import static cart.fixture.CouponFixture.테스트쿠폰2;
import static cart.fixture.MemberFixture.라잇;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private MemberCouponRepository memberCouponRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Test
    public void 발급한다() {
        Long couponId = 1L;
        MemberCouponRequest request = new MemberCouponRequest(
                LocalDateTime.now().plusDays(7)
        );
        when(couponRepository.findById(couponId)).thenReturn(테스트쿠폰1);

        couponService.issueCouponToMemberByCouponId(couponId, request, 라잇);

        verify(memberCouponRepository, times(1)).save(테스트쿠폰1, request.getExpiredAt(), 라잇);
    }

    @Test
    public void 선택한_장바구니에_대한_쿠폰_정보를_반환한다() {
        when(cartItemRepository.findByMember(라잇)).thenReturn(List.of(장바구니1, 장바구니2));
        when(memberCouponRepository.findUsableByMember(라잇)).thenReturn(List.of(CouponFixture.회원쿠폰1));

        AllOrderCouponResponse response = couponService.calculateCouponForCarts(라잇, List.of(1L, 2L));

        assertThat(response.getCoupons()).hasSize(1);
    }

    @Test
    public void 모든_쿠폰을_조회한다() {
        when(couponRepository.findAllCoupons()).thenReturn(List.of(테스트쿠폰1, 테스트쿠폰2));

        AllCouponResponse response = couponService.findAllCoupon();

        assertThat(response.getCoupons()).hasSize(2);
    }
}

