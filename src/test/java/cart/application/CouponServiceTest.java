package cart.application;

import cart.dao.CouponDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.dto.response.CouponResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CouponDao couponDao;

    @DisplayName("유저 쿠폰 발급 테스트")
    @Test
    void issue() {
        //given
        final Product product = productDao.getProductById(1L).orElseThrow();
        final Order order = new Order(List.of(new OrderProduct(product, 10)));
        final Member memberById = memberDao.getMemberById(1L);
        couponDao.deleteUserCoupon(memberById, 1L);
        final List<CouponResponse> memberCoupon = couponService.findMemberCoupon(memberById);

        assertThat(memberCoupon).isEmpty();

        //when
        couponService.issueCoupon(memberById, order);
        final List<CouponResponse> couponsAfterIssue = couponService.findMemberCoupon(memberById);

        //then
        assertThat(couponsAfterIssue).hasSize(1);
    }
}
