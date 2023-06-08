package cart.repository;

import cart.domain.Coupon;
import cart.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static cart.fixtures.CouponFixtures.*;
import static cart.fixtures.MemberFixtures.MEMBER1;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql({"/test-schema.sql", "/test-data.sql"})
class DBCouponRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        couponRepository = new DBCouponRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("Member의 쿠폰을 모두 가져온다.")
    void findByMemberIdTest() {
        // given
        Member member = MEMBER1;
        List<Coupon> memberCoupon = List.of(COUPON1, COUPON3);

        // when
        List<Coupon> findCoupons = couponRepository.findByMemberId(member.getId());

        // then
        assertThat(findCoupons).isEqualTo(memberCoupon);
    }

    @Test
    @DisplayName("ID에 해당하는 Coupon을 가져온다.")
    void findByIdTest() {
        // given
        Coupon coupon = COUPON1;

        // when
        Coupon findCoupon = couponRepository.findById(coupon.getId());

        // then
        assertThat(findCoupon).isEqualTo(coupon);
    }

    @Test
    @DisplayName("쿠폰을 저장한다.")
    void saveCouponTest() {
        // given
        Coupon newCouponToSave = NEW_COUPON_TO_INSERT;
        Coupon expectNewCoupon = NEW_COUPON;

        // when
        Coupon newCoupon = couponRepository.save(newCouponToSave);

        // then
        assertThat(newCoupon).isEqualTo(expectNewCoupon);
    }

    @Test
    @DisplayName("회원의 쿠폰을 삭제한다.")
    void deleteMemberCouponTest() {
        // given
        Coupon couponToDelete = COUPON1;
        Member member = MEMBER1;
        int memberCouponCountBeforeDelete = couponRepository.findByMemberId(member.getId()).size();
        int memberCouponCountAfterDelete = memberCouponCountBeforeDelete - 1;

        // when
        couponRepository.deleteMemberCoupon(member.getId(), couponToDelete.getId());

        // then
        assertThat(couponRepository.findByMemberId(member.getId())).hasSize(memberCouponCountAfterDelete);
    }
}