package cart.dao;

import static cart.domain.coupon.DiscountConditionType.MINIMUM_PRICE;
import static cart.domain.coupon.DiscountConditionType.NONE;
import static cart.domain.coupon.DiscountPolicyType.DELIVERY;
import static cart.domain.coupon.DiscountPolicyType.PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.entity.MemberEntity;
import cart.test.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class MemberCouponDaoTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private MemberCouponDao memberCouponDao;

    @Test
    void 사용자_쿠폰을_저장한다() {
        // given
        final CouponEntity couponEntity = couponDao.insert(new CouponEntity(
                "30000원 이상 3000원 할인 쿠폰",
                PRICE.name(), 30000, 0, false,
                MINIMUM_PRICE.name(), 20000
        ));
        final MemberEntity memberEntity = memberDao.insert(new MemberEntity("pizza1@pizza.com", "password"));
        final MemberCouponEntity memberCouponEntity = new MemberCouponEntity(
                couponEntity.getId(),
                memberEntity.getId(),
                false
        );

        // when
        memberCouponDao.insert(memberCouponEntity);

        // then
        assertThat(memberCouponDao.findAllUnusedMemberCouponByMemberId(memberCouponEntity.getMemberId())).hasSize(1);
    }

    @Test
    void 사용자_아이디를_입력받아_사용하지_않은_사용자의_쿠폰_엔티티를_조회한다() {
        // given
        final CouponEntity couponEntity1 = couponDao.insert(new CouponEntity(
                "30000원 이상 3000원 할인 쿠폰",
                PRICE.name(), 30000, 0, false,
                MINIMUM_PRICE.name(), 20000
        ));
        final CouponEntity couponEntity2 = couponDao.insert(new CouponEntity(
                "무료 배달 쿠폰",
                DELIVERY.name(), 0, 0, true,
                NONE.name(), 0
        ));
        final CouponEntity couponEntity3 = couponDao.insert(new CouponEntity(
                "무료 배달 쿠폰",
                DELIVERY.name(), 0, 0, true,
                NONE.name(), 0
        ));
        final MemberEntity memberEntity = memberDao.insert(new MemberEntity("pizza1@pizza.com", "password"));
        final MemberCouponEntity memberCouponEntity1 = memberCouponDao.insert(new MemberCouponEntity(
                couponEntity1.getId(),
                memberEntity.getId(),
                false
        ));
        final MemberCouponEntity memberCouponEntity2 = memberCouponDao.insert(new MemberCouponEntity(
                couponEntity2.getId(),
                memberEntity.getId(),
                false
        ));
        memberCouponDao.insert(new MemberCouponEntity(couponEntity3.getId(), memberEntity.getId(), true));

        // when
        final List<MemberCouponEntity> result = memberCouponDao.findAllUnusedMemberCouponByMemberId(
                memberEntity.getId());

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(memberCouponEntity1, memberCouponEntity2));
    }
}
