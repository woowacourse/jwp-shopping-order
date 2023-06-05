package cart.dao;

import static cart.fixture.CouponFixture._3만원_이상_3천원_할인_쿠폰_엔티티;
import static cart.fixture.CouponFixture._배달비_3천원_할인_쿠폰_엔티티;
import static cart.fixture.CouponFixture.쿠폰_엔티티_발급;
import static cart.fixture.MemberFixture.사용자1_엔티티;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.entity.CouponEntity;
import cart.entity.MemberEntity;
import cart.test.RepositoryTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@RepositoryTest
class CouponDaoTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private CouponDao couponDao;

    @Test
    void 쿠폰을_저장한다() {
        // given
        final MemberEntity member = memberDao.insert(사용자1_엔티티);
        final CouponEntity coupon = couponDao.insert(쿠폰_엔티티_발급(_3만원_이상_3천원_할인_쿠폰_엔티티, member.getId()));

        // when
        final CouponEntity savedCouponEntity = couponDao.insert(coupon);

        // then
        assertThat(couponDao.findById(savedCouponEntity.getId())).isPresent();
    }

    @Test
    void 쿠폰_아이디_리스트를_입력받아_쿠폰을_조회한다() {
        // given
        final MemberEntity member = memberDao.insert(사용자1_엔티티);
        final CouponEntity coupon1 = couponDao.insert(쿠폰_엔티티_발급(_3만원_이상_3천원_할인_쿠폰_엔티티, member.getId()));
        final CouponEntity coupon2 = couponDao.insert(쿠폰_엔티티_발급(_배달비_3천원_할인_쿠폰_엔티티, member.getId()));

        // when
        final List<CouponEntity> result = couponDao.findByIds(List.of(coupon1.getId(), coupon2.getId()));

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(coupon1, coupon2));
    }

    @Test
    void 단일_쿠폰을_조회한다() {
        // given
        final MemberEntity member = memberDao.insert(사용자1_엔티티);
        final CouponEntity coupon = couponDao.insert(쿠폰_엔티티_발급(_3만원_이상_3천원_할인_쿠폰_엔티티, member.getId()));

        // when
        final Optional<CouponEntity> result = couponDao.findById(coupon.getId());

        // then
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get()).usingRecursiveComparison().isEqualTo(coupon)
        );
    }

    @Test
    void 쿠폰의_USED_필드를_업데이트한다() {
        // given
        final MemberEntity member = memberDao.insert(사용자1_엔티티);
        final CouponEntity coupon = couponDao.insert(쿠폰_엔티티_발급(_3만원_이상_3천원_할인_쿠폰_엔티티, member.getId()));
        final CouponEntity couponEntity = new CouponEntity(coupon.getId(), coupon.getName(), coupon.getPolicyType(),
                coupon.getDiscountValue(), coupon.getMinimumPrice(), true, coupon.getMemberId());

        // when
        couponDao.updateUsed(couponEntity);

        // then
        assertAll(
                () -> assertThat(couponDao.findAllByUsedAndMemberId(false, member.getId())).isEmpty()
        );
    }
}
