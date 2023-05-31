package cart.dao;

import static cart.fixture.CouponFixture._3만원_이상_3천원_할인_쿠폰_엔티티;
import static cart.fixture.CouponFixture._배달비_3천원_할인_쿠폰_엔티티;
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
        final CouponEntity couponEntity = couponDao.insert(_3만원_이상_3천원_할인_쿠폰_엔티티);

        // when
        final CouponEntity savedCouponEntity = couponDao.insert(couponEntity);

        // then
        assertThat(couponDao.findById(savedCouponEntity.getId())).isPresent();
    }

    @Test
    void 쿠폰_아이디_리스트를_입력받아_쿠폰을_조회한다() {
        // given
        final CouponEntity couponEntity1 = couponDao.insert(_3만원_이상_3천원_할인_쿠폰_엔티티);
        final CouponEntity couponEntity2 = couponDao.insert(_배달비_3천원_할인_쿠폰_엔티티);

        // when
        final List<CouponEntity> result = couponDao.findByIds(List.of(couponEntity1.getId(), couponEntity2.getId()));

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(couponEntity1, couponEntity2));
    }

    @Test
    void 단일_쿠폰을_조회한다() {
        // given
        final CouponEntity couponEntity = couponDao.insert(_3만원_이상_3천원_할인_쿠폰_엔티티);

        // when
        final Optional<CouponEntity> result = couponDao.findById(couponEntity.getId());

        // then
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get()).usingRecursiveComparison().isEqualTo(couponEntity)
        );
    }
}
