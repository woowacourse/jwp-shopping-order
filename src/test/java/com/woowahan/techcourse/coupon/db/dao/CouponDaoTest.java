package com.woowahan.techcourse.coupon.db.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.woowahan.techcourse.coupon.domain.Coupon;
import com.woowahan.techcourse.coupon.domain.Order;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class CouponDaoTest {

    private JdbcTemplate jdbcTemplate;
    private CouponDao couponDao;

    @Autowired
    void setUp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        couponDao = new CouponDao(jdbcTemplate);
    }

    @BeforeEach
    void setDummyCoupon() {
        jdbcTemplate.update("INSERT INTO amount_discount (id, rate) VALUES (1, 10)");
        jdbcTemplate.update(
                "INSERT INTO discount_type (id, discount_type, discount_amount_id) VALUES (1, 'PERCENT', 1)");
        jdbcTemplate.update("INSERT INTO discount_condition (id, discount_condition_type) VALUES (1, 'ALWAYS')");
        jdbcTemplate.update(
                "INSERT INTO coupon(id, name, discount_type_id, discount_condition_id) VALUES (1, '10% 할인 쿠폰', 1, 1)");
        jdbcTemplate.execute("INSERT INTO coupon_member(id, coupon_id, member_id) VALUES (1, 1, 1)");
    }

    @ParameterizedTest
    @CsvSource(value = {"1, true", "2, false"})
    void 쿠폰_존재_여부를_반환한다(long couponId, boolean expected) {
        // given
        // when
        boolean result = couponDao.exists(couponId);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findById() {
        Optional<Coupon> result = couponDao.findById(1L);

        assertSoftly(softly -> {
            softly.assertThat(result).isPresent();
            softly.assertThat(result.get().getCouponId()).isEqualTo(1L);
            softly.assertThat(result.get().getName().getValue()).isEqualTo("10% 할인 쿠폰");
            softly.assertThat(result.get().getDiscountCondition().isSatisfiedBy(null)).isTrue();
            softly.assertThat(result.get().getDiscountPolicy().calculateDiscountAmount(new Order(100L)).getValue())
                    .isEqualTo(10);
        });
    }

    @Test
    void 없는_id_는_empty_가_반환된다() {
        Optional<Coupon> result = couponDao.findById(2L);

        assertThat(result).isEmpty();
    }

    @Test
    void 멤버_id_를_통해_쿠폰을_조회할_수_있다() {
        List<Coupon> result = couponDao.findAllByMemberId(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void 없는_멤버_id_를_통해_쿠폰을_조회하면_empty_가_반환된다() {
        List<Coupon> result = couponDao.findAllByMemberId(2L);

        assertThat(result).isEmpty();
    }

    @Test
    void 쿠폰을_id로_여러개_조회할_수_있다() {
        List<Coupon> result = couponDao.findAllByIds(List.of(1L, 2L));

        assertThat(result).hasSize(1);
    }
}
