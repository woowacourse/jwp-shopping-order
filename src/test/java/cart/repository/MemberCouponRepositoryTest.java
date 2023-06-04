package cart.repository;

import static cart.fixture.TestFixture.밀리;
import static cart.fixture.TestFixture.쿠폰_10퍼센트;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.MemberDao;
import cart.domain.coupon.MemberCoupon;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.entity.MemberEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MemberCouponRepositoryTest {

    @InjectMocks
    private MemberCouponRepository memberCouponRepository;

    @Mock
    private MemberCouponDao memberCouponDao;

    @Mock
    private CouponDao couponDao;

    @Mock
    private MemberDao memberDao;

    @Test
    void 멤버_쿠폰을_id로_조회한다() {
        // given
        given(memberCouponDao.findById(any()))
                .willReturn(Optional.of(new MemberCouponEntity(1L, 1L, 1L, LocalDate.of(3000, 6, 16))));
        given(couponDao.findById(1L))
                .willReturn(Optional.of(new CouponEntity(1L, "쿠폰", "RATE", BigDecimal.valueOf(10), BigDecimal.ZERO)));
        given(memberDao.findById(1L))
                .willReturn(Optional.of(new MemberEntity(1L, "millie@email.com", "millie")));

        // when
        Optional<MemberCoupon> findMemberCoupon = memberCouponRepository.findById(1L);

        // then
        assertThat(findMemberCoupon).isPresent();
        assertThat(findMemberCoupon.get().getId()).isEqualTo(1L);
    }

    @Test
    void 없는_멤버_쿠폰을_조회하면_빈_값을_반환한다() {
        // given
        given(memberCouponDao.findById(any()))
                .willReturn(Optional.empty());

        // when
        Optional<MemberCoupon> findMemberCoupon = memberCouponRepository.findById(1L);

        // then
        verify(couponDao, never()).findById(any());
        verify(memberDao, never()).findById(any());
        assertThat(findMemberCoupon).isEmpty();
    }

    @Test
    void 멤버_쿠폰을_저장한다() {
        // given
        MemberCoupon memberCoupon = new MemberCoupon(밀리, 쿠폰_10퍼센트);
        given(memberCouponDao.save(any()))
                .willReturn(1L);

        // when
        MemberCoupon savedMemberCoupon = memberCouponRepository.save(memberCoupon);

        // then
        assertThat(savedMemberCoupon.getId()).isEqualTo(1L);
        assertThat(savedMemberCoupon).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(memberCoupon);
    }

    @Test
    void 멤버_쿠폰을_삭제한다() {
        // given
        MemberCoupon memberCoupon = new MemberCoupon(밀리, 쿠폰_10퍼센트);

        // when
        memberCouponRepository.delete(memberCoupon);

        // then
        verify(memberCouponDao, times(1)).deleteById(any());
    }

    @Test
    void 만료기간이_지나지_않은_쿠폰을_멤버로_조회한다() {
        given(memberCouponDao.findByMemberId(any()))
                .willReturn(List.of(
                        new MemberCouponEntity(1L, 1L, 1L, LocalDate.of(3000, 6, 16)),
                        new MemberCouponEntity(2L, 1L, 1L, LocalDate.of(1000, 6, 16)),
                        new MemberCouponEntity(3L, 1L, 1L, LocalDate.of(3000, 6, 8))
                ));
        given(couponDao.findById(1L))
                .willReturn(Optional.of(new CouponEntity(1L, "쿠폰", "RATE", BigDecimal.valueOf(10), BigDecimal.ZERO)));
        given(memberDao.findById(1L))
                .willReturn(Optional.of(new MemberEntity(1L, "millie@email.com", "millie")));

        // when
        List<MemberCoupon> memberCoupons = memberCouponRepository.findNotExpiredAllByMember(밀리);

        // then
        assertThat(memberCoupons).hasSize(2);
        assertThat(memberCoupons).map(MemberCoupon::getId)
                .containsExactly(1L, 3L);
    }
}
