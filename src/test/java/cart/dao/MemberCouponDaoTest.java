package cart.dao;

import static fixture.CouponFixture.정액_할인_쿠폰;
import static fixture.CouponFixture.할인율_쿠폰;
import static fixture.MemberCouponFixture.쿠폰_유저_1_정액_할인_쿠폰;
import static fixture.MemberFixture.유저_1;
import static fixture.MemberFixture.유저_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import anotation.RepositoryTest;
import cart.dao.dto.MemberCouponDto;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class MemberCouponDaoTest {

    @Autowired
    private MemberCouponDao memberCouponDao;

    @Test
    @DisplayName("Member Coupon 저장을 확인한다.")
    void createMemberCoupon_success() {
        MemberCouponDto memberCouponDto = new MemberCouponDto(유저_2.getId(), 정액_할인_쿠폰.getId());

        Long insert = memberCouponDao.insert(memberCouponDto);

        MemberCouponDto memberCouponDtoAfterSave = memberCouponDao.findById(insert)
                .orElseThrow(NoSuchElementException::new);
        assertThat(memberCouponDtoAfterSave).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(memberCouponDto);
    }

    @Test
    @DisplayName("Member Coupon Dto 를 조회하는 기능 테스트")
    void findByIdTest() {
        MemberCouponDto memberCouponDto = memberCouponDao.findById(쿠폰_유저_1_정액_할인_쿠폰.getId())
                .orElseThrow(NoSuchElementException::new);

        assertThat(memberCouponDto)
                .extracting(MemberCouponDto::getId, MemberCouponDto::getMemberId, MemberCouponDto::getCouponId)
                .containsExactly(1L, 유저_1.getId(), 정액_할인_쿠폰.getId());
    }

    @Test
    @DisplayName("찾는 Member Coupon Dto 가 없는 경우 빈 Optional 을 반환한다.")
    void findById_returnEmpty() {
        Optional<MemberCouponDto> memberCouponDto = memberCouponDao.findById(100L);

        assertThat(memberCouponDto).isNotPresent();
    }

    @Test
    @DisplayName("Member id 로 해당하는 Member Coupon 을 찾는다.")
    void findByMemberId() {
        List<MemberCouponDto> memberCouponDtos = memberCouponDao.findByMemberId(유저_1.getId());

        assertThat(memberCouponDtos)
                .extracting(MemberCouponDto::getId, MemberCouponDto::getMemberId, MemberCouponDto::getCouponId)
                .containsExactly(tuple(1L, 유저_1.getId(), 정액_할인_쿠폰.getId()), tuple(2L, 유저_1.getId(), 할인율_쿠폰.getId()));
    }

    @Test
    @DisplayName("MemberCouponId 로 Member Coupon 을 삭제한다.")
    void deleteById() {
        memberCouponDao.deleteById(쿠폰_유저_1_정액_할인_쿠폰.getId());

        Optional<MemberCouponDto> nullMemberCoupon = memberCouponDao.findById(쿠폰_유저_1_정액_할인_쿠폰.getId());

        assertThatThrownBy(nullMemberCoupon::orElseThrow)
                .isInstanceOf(NoSuchElementException.class);
    }

}