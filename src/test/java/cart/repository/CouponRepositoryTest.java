package cart.repository;

import static fixture.CouponFixture.COUPON_1_NOT_NULL_PRICE;
import static fixture.MemberCouponFixture.MEMBER_COUPON_1;
import static fixture.MemberCouponFixture.MEMBER_COUPON_2;
import static fixture.MemberFixture.MEMBER_1;
import static fixture.MemberFixture.MEMBER_3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import anotation.RepositoryTest;
import cart.dao.MemberCouponDao;
import cart.domain.Coupon;
import cart.domain.MemberCoupon;
import cart.dto.CouponDto;
import cart.dto.MemberCouponDto;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private MemberCouponDao memberCouponDao;

    @Test
    @DisplayName("사용자가 쿠폰을 추가하면, 현재 Coupon 테이블에 있는 쿠폰이 모두 추가된다.")
    void addCoupon() {
        couponRepository.saveCoupon(MEMBER_3);

        List<MemberCouponDto> memberCouponDtos = memberCouponDao.findByMemberId(MEMBER_3.getId());

        assertThat(memberCouponDtos)
                .extracting(MemberCouponDto::getMemberId, MemberCouponDto::getCouponId)
                .containsExactly(tuple(3L, 1L), tuple(3L, 2L));
    }

    @Test
    @DisplayName("Member 의 정보를 주면, Member 가 가진 Coupon 을 반환받는다.")
    void findMemberCouponByMember() {
        List<MemberCoupon> memberCoupons = couponRepository.findMemberCouponByMember(MEMBER_1);

        assertThat(memberCoupons).usingRecursiveComparison()
                .isEqualTo(List.of(MEMBER_COUPON_1, MEMBER_COUPON_2));
    }

    @Test
    @DisplayName("Member Coupon Id 로 해당하는 Coupon 을 가져온다.")
    void findByCouponByMemberCouponId() {
        Coupon coupon = couponRepository.findCouponByMemberCouponId(1L).orElseThrow(NoSuchElementException::new);

        assertThat(coupon).usingRecursiveComparison()
                .isEqualTo(COUPON_1_NOT_NULL_PRICE);
    }

    @Test
    @DisplayName("Member Coupon Id 로 Member Coupon 을 삭제한다.")
    void deleteMemberCouponById() {
        couponRepository.deleteMemberCouponById(Optional.of(1L));
        List<MemberCouponDto> memberCouponDtos = memberCouponDao.findByMemberId(MEMBER_1.getId());

        assertThat(memberCouponDtos).hasSize(1)
                .extracting(MemberCouponDto::getMemberId, MemberCouponDto::getCouponId)
                .containsExactly(tuple(1L, 2L));
    }

}