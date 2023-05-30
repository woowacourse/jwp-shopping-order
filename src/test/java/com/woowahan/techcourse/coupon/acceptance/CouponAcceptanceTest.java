package com.woowahan.techcourse.coupon.acceptance;

import com.woowahan.techcourse.common.acceptance.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
class CouponAcceptanceTest extends IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setDummyData() {
        jdbcTemplate.execute("INSERT INTO amount_discount (id, rate) VALUES (1, 10)");
        jdbcTemplate.execute(
                "INSERT INTO discount_type (id, discount_type, discount_amount_id) VALUES (1, 'PERCENT', 1)");
        jdbcTemplate.execute("INSERT INTO discount_condition (id, discount_condition_type) VALUES (1, 'ALWAYS')");
        jdbcTemplate.execute(
                "INSERT INTO coupon(id, name, discount_type_id, discount_condition_id) VALUES (1, '10% 할인 쿠폰', 1, 1)");
        jdbcTemplate.execute("INSERT INTO coupon_member(id, coupon_id, member_id) VALUES (1, 1, 1)");
    }

    @Test
    void 전체_쿠폰_목록을_조회할_수_있다() {
        var 결과 = CouponStep.쿠폰_전체_조회();

        CouponStep.쿠폰_사이즈는_N이다(결과, 1);
    }
}
