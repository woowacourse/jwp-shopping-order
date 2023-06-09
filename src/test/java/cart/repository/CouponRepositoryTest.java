package cart.repository;

import static fixture.CouponFixture.정액_할인_쿠폰;
import static fixture.MemberCouponFixture.쿠폰_유저_1_정액_할인_쿠폰;
import static fixture.MemberCouponFixture.쿠폰_유저_1_할인율_쿠폰;
import static fixture.MemberFixture.유저_1;
import static fixture.MemberFixture.유저_2;
import static fixture.MemberFixture.아무것도_가지지_않은_유저;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import anotation.RepositoryTest;
import cart.dao.MemberCouponDao;
import cart.dao.dto.MemberCouponDto;
import cart.domain.Coupon;
import cart.domain.MemberCoupon;
import cart.exception.CouponNotFoundException;
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
        couponRepository.saveCoupon(아무것도_가지지_않은_유저, 정액_할인_쿠폰.getId());

        List<MemberCouponDto> memberCouponDtos = memberCouponDao.findByMemberId(아무것도_가지지_않은_유저.getId());

        assertThat(memberCouponDtos)
                .extracting(MemberCouponDto::getMemberId, MemberCouponDto::getCouponId)
                .containsExactly(tuple(아무것도_가지지_않은_유저.getId(), 정액_할인_쿠폰.getId()));
    }

    @Test
    @DisplayName("Member 의 정보를 주면, Member 가 가진 Coupon 을 반환받는다.")
    void findMemberCouponByMember() {
        List<MemberCoupon> memberCoupons = couponRepository.findMemberCouponByMember(유저_1);

        assertThat(memberCoupons).usingRecursiveComparison()
                .isEqualTo(List.of(쿠폰_유저_1_정액_할인_쿠폰, 쿠폰_유저_1_할인율_쿠폰));
    }

    @Test
    @DisplayName("Member Coupon Id 와 Member Id 에 해당하는 Coupon 을 가져온다. (성공)")
    void findByCouponByMemberCouponId_success() {
        Coupon coupon = couponRepository.findCouponByMemberAndMemberCouponId(유저_1, 쿠폰_유저_1_정액_할인_쿠폰.getId()).orElseThrow(NoSuchElementException::new);

        assertThat(coupon).usingRecursiveComparison()
                .isEqualTo(정액_할인_쿠폰);
    }

    @Test
    @DisplayName("Member Coupon Id 와 Member Id 에 해당하는 Coupon 을 가져온다. (성공)")
    void findByCouponByMemberCouponId_fail() {
        assertThatThrownBy(() -> couponRepository.findCouponByMemberAndMemberCouponId(유저_2, 쿠폰_유저_1_정액_할인_쿠폰.getId()))
                .isInstanceOf(CouponNotFoundException.class);
    }

    @Test
    @DisplayName("Member Coupon Id 로 Member Coupon 을 삭제한다.")
    void deleteMemberCouponById() {
        couponRepository.deleteMemberCouponById(Optional.of(쿠폰_유저_1_정액_할인_쿠폰.getId()));
        List<MemberCouponDto> memberCouponDtos = memberCouponDao.findByMemberId(유저_1.getId());

        assertThat(memberCouponDtos).hasSize(1)
                .extracting(MemberCouponDto::getMemberId, MemberCouponDto::getCouponId)
                .containsExactly(tuple(유저_1.getId(), 쿠폰_유저_1_할인율_쿠폰.getId()));
    }

}