package cart.dao;

import static fixture.MemberFixture.MEMBER_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import anotation.RepositoryTest;
import cart.dto.MemberCouponDto;
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
        MemberCouponDto memberCouponDto = new MemberCouponDto(1L, 2L, 3L);

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
        MemberCouponDto memberCouponDto = memberCouponDao.findById(1L)
                .orElseThrow(NoSuchElementException::new);

        assertThat(memberCouponDto)
                .extracting(MemberCouponDto::getId, MemberCouponDto::getMemberId, MemberCouponDto::getCouponId)
                .containsExactly(1L, 1L, 1L);
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
        List<MemberCouponDto> memberCouponDtos = memberCouponDao.findByMemberId(MEMBER_1.getId());

        assertThat(memberCouponDtos)
                .extracting(MemberCouponDto::getId, MemberCouponDto::getMemberId, MemberCouponDto::getCouponId)
                .containsExactly(tuple(1L, 1L, 1L), tuple(2L, 1L, 2L));
    }

    @Test
    @DisplayName("MemberCouponId 로 Member Coupon 을 삭제한다.")
    void deleteById() {
        memberCouponDao.deleteById(1L);

        Optional<MemberCouponDto> nullMemberCoupon = memberCouponDao.findById(1L);

        assertThatThrownBy(nullMemberCoupon::orElseThrow)
                .isInstanceOf(NoSuchElementException.class);
    }

}