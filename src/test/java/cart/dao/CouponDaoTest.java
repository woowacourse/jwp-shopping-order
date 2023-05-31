package cart.dao;

import cart.entity.CouponEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
class CouponDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CouponDao couponDao;

    @BeforeEach
    void setUp() {
        this.couponDao = new CouponDao(jdbcTemplate);
    }

    @Test
    void 쿠폰을_저장한다() {
        // given
        final CouponEntity entity = new CouponEntity("30000원 이상 3000원 할인 쿠폰", "PRICE", 3000L, 30000L);

        // when
        final CouponEntity result = couponDao.insert(entity);

        // then
        assertThat(couponDao.findAll().size()).isEqualTo(1);
    }

    @Test
    void 단일_쿠폰을_조회한다() {
        // given
        final CouponEntity entity = new CouponEntity("30000원 이상 3000원 할인 쿠폰", "PRICE", 3000L, 30000L);
        final CouponEntity insert = couponDao.insert(entity);

        // when
        final CouponEntity result = couponDao.findById(insert.getId()).get();

        // then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo(insert.getName()),
                () -> assertThat(result.getDiscountValue()).isEqualTo(insert.getDiscountValue()),
                () -> assertThat(result.getPolicyType()).isEqualTo(insert.getPolicyType()),
                () -> assertThat(result.getMinimumPrice()).isEqualTo(insert.getMinimumPrice())
        );
    }

    @Test
    void 입력받은_아이디에_대한_쿠폰을_조회한다() {
        // given
        final CouponEntity entity1 = new CouponEntity("10000원 이상 1000원 할인 쿠폰", "PRICE", 1000L, 10000L);
        final CouponEntity entity2 = new CouponEntity("30000원 이상 3000원 할인 쿠폰", "PRICE", 3000L, 30000L);
        final CouponEntity entity3 = new CouponEntity("50000원 이상 5000원 할인 쿠폰", "PRICE", 5000L, 50000L);
        final CouponEntity insert1 = couponDao.insert(entity1);
        final CouponEntity insert2 = couponDao.insert(entity2);
        final CouponEntity insert3 = couponDao.insert(entity3);

        // when
        final List<CouponEntity> couponEntities = couponDao.findByIds(List.of(insert1.getId(), insert2.getId(), insert3.getId()));

        // then
        assertThat(couponEntities.size()).isEqualTo(3);
    }

    @Test
    void 모든_쿠폰을_조회한다() {
        // given
        final CouponEntity entity1 = new CouponEntity("10000원 이상 1000원 할인 쿠폰", "PRICE", 1000L, 10000L);
        final CouponEntity entity2 = new CouponEntity("30000원 이상 3000원 할인 쿠폰", "PRICE", 3000L, 30000L);
        final CouponEntity entity3 = new CouponEntity("50000원 이상 5000원 할인 쿠폰", "PRICE", 5000L, 50000L);
        final CouponEntity insert1 = couponDao.insert(entity1);
        final CouponEntity insert2 = couponDao.insert(entity2);
        final CouponEntity insert3 = couponDao.insert(entity3);

        // when
        final List<CouponEntity> couponEntities = couponDao.findAll();

        // then
        assertThat(couponEntities.size()).isEqualTo(3);
    }

    @Test
    void 쿠폰을_수정한다() {
        // given
        final CouponEntity entity1 = new CouponEntity("10000원 이상 1000원 할인 쿠폰", "PRICE", 1000L, 10000L);
        final CouponEntity insert1 = couponDao.insert(entity1);
        final CouponEntity update = new CouponEntity(insert1.getId(), "30000원 이상 3000원 할인 쿠폰", "PRICE", 3000L, 30000L);
        // when
        couponDao.update(update);

        // then
        final CouponEntity result = couponDao.findById(update.getId()).get();
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(update.getId()),
                () -> assertThat(result.getName()).isEqualTo(update.getName()),
                () -> assertThat(result.getPolicyType()).isEqualTo(update.getPolicyType()),
                () -> assertThat(result.getDiscountValue()).isEqualTo(update.getDiscountValue()),
                () -> assertThat(result.getMinimumPrice()).isEqualTo(update.getMinimumPrice())
        );
    }
}