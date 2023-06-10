package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.MemberDao;
import cart.dao.entity.CouponEntity;
import cart.dao.entity.MemberCouponEntity;
import cart.domain.coupon.EmptyMemberCoupon;
import cart.domain.coupon.MemberCoupon;
import cart.domain.coupon.UsedMemberCoupon;
import cart.exception.MemberCouponException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static cart.fixture.CouponFixture.*;
import static cart.fixture.MemberFixture.라잇;
import static cart.fixture.MemberFixture.라잇_엔티티;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberCouponRepositoryTest {
    @Mock
    private MemberCouponDao memberCouponDao;

    @Mock
    private CouponDao couponDao;

    @Mock
    private MemberDao memberDao;

    @InjectMocks
    private MemberCouponRepository memberCouponRepository;

    @Test
    void 저장한다() {
        when(memberCouponDao.save(MemberCouponEntity.from(회원쿠폰1_아이디_널))).thenReturn(1L);

        Long savedMemberCouponId = memberCouponRepository.save(회원쿠폰1_아이디_널.getCoupon(), 회원쿠폰1_아이디_널.getExpiredAt(), 회원쿠폰1_아이디_널.getMember());

        assertThat(savedMemberCouponId).isEqualTo(1L);
    }

    @Test
    void 멤버로_사용가능한_쿠폰을_조회한다() {
        List<CouponEntity> couponEntities = List.of(테스트쿠폰1_엔티티, 테스트쿠폰2_엔티티);
        List<MemberCouponEntity> memberCouponEntities = List.of(MemberCouponEntity.from(회원쿠폰1));
        when(couponDao.findAll()).thenReturn(couponEntities);
        when(memberCouponDao.findUsableByMemberId(라잇.getId())).thenReturn(memberCouponEntities);

        List<MemberCoupon> usableMemberCoupons = memberCouponRepository.findUsableByMember(라잇);

        assertThat(usableMemberCoupons).contains(회원쿠폰1);
    }

    @Test
    void 아이디로_조회한다() {
        when(memberCouponDao.findById(회원쿠폰1.getId())).thenReturn(Optional.of(MemberCouponEntity.from(회원쿠폰1)));
        when(couponDao.findById(테스트쿠폰1.getCouponInfo().getId())).thenReturn(Optional.of(테스트쿠폰1_엔티티));
        when(memberDao.findById(라잇.getId())).thenReturn(Optional.of(라잇_엔티티));

        MemberCoupon memberCoupon = memberCouponRepository.findById(회원쿠폰1.getId());

        assertThat(memberCoupon).isEqualTo(회원쿠폰1);
    }

    @Test
    void 아이디로_쿠폰을_조회시_없으면_예외를_발생한다() {
        Long id = 1L;

        when(memberCouponDao.findById(id)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> memberCouponRepository.findById(id))
                .isInstanceOf(MemberCouponException.NoExist.class);
    }

    @Test
    void 수정한다() {
        MemberCoupon usedMemberCoupon = new UsedMemberCoupon(
                회원쿠폰1.getId(),
                회원쿠폰1.getCoupon(),
                회원쿠폰1.getMember(),
                회원쿠폰1.getExpiredAt(),
                회원쿠폰1.getCreatedAt()
        );
        MemberCouponEntity expected = MemberCouponEntity.from(usedMemberCoupon);

        // When
        memberCouponRepository.update(usedMemberCoupon);

        verify(memberCouponDao).update(expected);
    }

    @Test
    void 빈_쿠폰은_아무_일도_일어나지_않는다() {
        MemberCoupon emptyMemberCoupon = new EmptyMemberCoupon();

        memberCouponRepository.update(emptyMemberCoupon);

        verify(memberCouponDao, times(0)).update(any());
    }
}
