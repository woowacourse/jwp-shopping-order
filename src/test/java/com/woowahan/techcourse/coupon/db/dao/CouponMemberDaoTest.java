package com.woowahan.techcourse.coupon.db.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class CouponMemberDaoTest {

    private JdbcTemplate jdbcTemplate;
    private CouponMemberDao couponMemberDao;

    @Autowired
    void setUp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        couponMemberDao = new CouponMemberDao(jdbcTemplate);
    }

    @BeforeEach
    void setDummyCoupon() {
        jdbcTemplate.update("INSERT INTO amount_discount (id, rate) VALUES (1, 10)");
        jdbcTemplate.update(
                "INSERT INTO discount_type (id, discount_type, discount_amount_id) VALUES (1, 'PERCENT', 1)");
        jdbcTemplate.update("INSERT INTO discount_condition (id, discount_condition_type) VALUES (1, 'ALWAYS')");
        jdbcTemplate.update(
                "INSERT INTO coupon(id, name, discount_type_id, discount_condition_id) VALUES (1, '10% 할인 쿠폰', 1, 1)");
    }

    @Test
    void 멤버가_쿠폰을_가지고_있지_않으면_false() {
        // given
        Long memberId = 1L;
        Long couponId = 1L;

        // when
        boolean result = couponMemberDao.exists(couponId, memberId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 멤버가_쿠폰을_갖게_한다() {
        // given
        Long memberId = 1L;
        Long couponId = 1L;

        // when
        couponMemberDao.insert(couponId, memberId);

        // then
        assertThat(couponMemberDao.exists(couponId, memberId)).isTrue();
    }

    @Test
    void 멤버에게서_쿠폰을_제거한다() {
        // given
        Long memberId = 1L;
        Long couponId = 1L;
        couponMemberDao.insert(couponId, memberId);

        // when
        couponMemberDao.delete(couponId, memberId);

        // then
        assertThat(couponMemberDao.exists(couponId, memberId)).isFalse();
    }
}
