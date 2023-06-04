package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.vo.Amount;
import java.util.List;
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
    private final Coupon coupon1 = new Coupon("name1", Amount.of(1_000), Amount.of(10_000), false);
    private final Coupon coupon2 = new Coupon("name2", Amount.of(2_000), Amount.of(20_000), true);

    @Autowired
    private CouponDao couponDao;
    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("쿠폰 아이디와 멤버 아이디로 쿠폰을 찾는다.")
    void testFindByCouponIdAndMemberId() {
        //given
        final Member savedMember = memberDao.addMember(member);
        final Coupon savedCoupon = couponDao.save(coupon1, savedMember.getId());

        //when
        final Optional<Coupon> optionalCoupon = couponDao.findByCouponIdAndMemberId(savedCoupon.getId(),
            savedMember.getId());

        //then
        assertThat(optionalCoupon).isPresent();
        final Coupon coupon = optionalCoupon.get();
        assertThat(coupon.getName()).isEqualTo(this.coupon1.getName());
    }

    @Test
    @DisplayName("쿠폰을 업데이트한다.")
    void testUpdate() {
        //given
        final Member savedMember = memberDao.addMember(member);
        final Coupon savedCoupon = couponDao.save(coupon1, savedMember.getId());
        final Coupon usedCoupon = savedCoupon.use();

        //when
        couponDao.update(usedCoupon, savedMember.getId());

        //then
        final Coupon coupon = couponDao.findByCouponIdAndMemberId(usedCoupon.getId(), savedMember.getId())
            .orElseThrow(RuntimeException::new);
        assertThat(coupon.isUsed()).isTrue();
    }

    @Test
    @DisplayName("모든 쿠폰을 조회한다.")
    void testFindAll() {
        //given
        final Member savedMember = memberDao.addMember(member);
        final Coupon savedCoupon1 = couponDao.save(coupon1, savedMember.getId());
        final Coupon savedCoupon2 = couponDao.save(coupon2, savedMember.getId());

        //when
        final List<Coupon> coupons = couponDao.findAll();

        //then
        final List<Coupon> expectedCoupons = List.of(savedCoupon1, savedCoupon2);
        for (int index = 0; index < coupons.size(); index++) {
            assertThat(coupons.get(index).getId()).isEqualTo(expectedCoupons.get(index).getId());
        }
    }

    @Test
    @DisplayName("id로 쿠폰을 찾는다.")
    void testFindById() {
        //given
        final Member savedMember = memberDao.addMember(member);
        final Coupon savedCoupon = couponDao.save(coupon1, savedMember.getId());

        //when
        final Coupon coupon = couponDao.findById(savedCoupon.getId())
            .orElseThrow(RuntimeException::new);

        //then
        assertThat(coupon).usingRecursiveComparison().isEqualTo(savedCoupon);
    }
}
