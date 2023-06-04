package cart.repository;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.repository.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Transactional
class CouponRepositoryTest {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("쿠폰을 발급한다")
    void publishCoupon() {
        Member member = memberDao.findById(1L);

        assertDoesNotThrow(() -> couponRepository.save(member, 1L));
    }

    @Test
    @DisplayName("사용자 쿠폰을 조회한다")
    void getCoupon() {
        Member member = memberDao.findById(1L);
        couponRepository.save(member, 1L);
        List<Coupon> memberCoupons = couponRepository.findByMemberId(member);
        assertThat(memberCoupons.get(0).getName()).isEqualTo("5000원 할인 쿠폰");
    }

    @Test
    @DisplayName("쿠폰을 조회한다")
    void getCoupons() {
        assertThat(couponRepository.findAll().size()).isEqualTo(3);
    }
}
