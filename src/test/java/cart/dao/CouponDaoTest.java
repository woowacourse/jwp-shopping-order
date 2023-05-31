package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.vo.Amount;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

@JdbcTest(includeFilters = {
    @Filter(type = FilterType.ANNOTATION, value = Repository.class)
})
class CouponDaoTest {

    private final Member member = new Member(1L, "test@test.com", "password");
    private final Coupon coupon = new Coupon("name", Amount.of(1_000), Amount.of(10_000), false);

    @Autowired
    private CouponDao couponDao;
    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("쿠폰 아이디와 멤버 아이디로 쿠폰을 찾는다.")
    void testFindByCouponIdAndMemberId() {
        //given
        final Member savedMember = memberDao.addMember(member);
        final Coupon savedCoupon = couponDao.save(coupon, savedMember.getId());

        //when
        final Optional<Coupon> optionalCoupon = couponDao.findByCouponIdAndMemberId(savedCoupon.getId(),
            savedMember.getId());

        //then
        assertThat(optionalCoupon).isPresent();
        final Coupon coupon = optionalCoupon.get();
        assertThat(coupon.getName()).isEqualTo(this.coupon.getName());
    }

    @Test
    @DisplayName("쿠폰을 업데이트한다.")
    void testUpdate() {
        //given
        final Member savedMember = memberDao.addMember(member);
        final Coupon savedCoupon = couponDao.save(coupon, savedMember.getId());
        final Coupon usedCoupon = savedCoupon.use();

        //when
        couponDao.update(usedCoupon, savedMember.getId());

        //then
        final Coupon coupon = couponDao.findByCouponIdAndMemberId(usedCoupon.getId(), savedMember.getId())
            .orElseThrow(RuntimeException::new);
        assertThat(coupon.isUsed()).isTrue();
    }
}
