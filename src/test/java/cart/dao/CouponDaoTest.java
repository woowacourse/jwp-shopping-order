package cart.dao;

import static cart.domain.coupon.DiscountConditionType.MINIMUM_PRICE;
import static cart.domain.coupon.DiscountConditionType.NONE;
import static cart.domain.coupon.DiscountPolicyType.DELIVERY;
import static cart.domain.coupon.DiscountPolicyType.PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.entity.CouponEntity;
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
    private CouponDao couponDao;

    @Test
    void 쿠폰을_저장한다() {
        // given
        final CouponEntity couponEntity = new CouponEntity(
                "30000원 이상 3000원 할인 쿠폰",
                PRICE.name(), 30000, 0, false,
                MINIMUM_PRICE.name(), 20000
        );

        // when
        final CouponEntity savedCouponEntity = couponDao.insert(couponEntity);

        // then
        assertThat(couponDao.findById(savedCouponEntity.getId())).isPresent();
    }

    @Test
    void 쿠폰_아이디_리스트를_입력받아_쿠폰을_조회한다() {
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

        // when
        final List<CouponEntity> result = couponDao.findByIds(List.of(couponEntity1.getId(), couponEntity2.getId()));

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(couponEntity1, couponEntity2));
    }

    @Test
    void 단일_쿠폰을_조회한다() {
        // given
        final CouponEntity couponEntity = couponDao.insert(new CouponEntity(
                "30000원 이상 3000원 할인 쿠폰",
                PRICE.name(), 30000, 0, false,
                MINIMUM_PRICE.name(), 20000
        ));

        // when
        final Optional<CouponEntity> result = couponDao.findById(couponEntity.getId());

        // then
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get()).usingRecursiveComparison().isEqualTo(couponEntity)
        );
    }
}
