package com.woowahan.techcourse.order.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.woowahan.techcourse.order.domain.OrderCoupon;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class OrderCouponDaoTest {

    private final OrderCouponDao orderCouponDao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderCouponDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        orderCouponDao = new OrderCouponDao(jdbcTemplate);
    }

    @BeforeEach
    void setOrder() {
        jdbcTemplate.execute(
                "INSERT INTO orders (id, member_id, actual_price, original_price) VALUES (1, 1, 9000, 10000)");
    }

    @Test
    void 주문_쿠폰_생성_조회_삭제_테스트() {
        // given
        long orderId = 1L;

        // when
        orderCouponDao.insertAll(orderId, List.of(1L, 2L, 3L));

        // then
        List<OrderCoupon> orderCoupons = orderCouponDao.findAllByOrderId(orderId);
        assertThat(orderCoupons).hasSize(3);
    }

    @Test
    void 없는_주문을_기준으로_찾으면_아무것도_나오지_않는다() {
        // given
        long orderId = 1L;

        // when
        orderCouponDao.insertAll(orderId, List.of(1L, 2L, 3L));

        // then
        List<OrderCoupon> orderCoupons = orderCouponDao.findAllByOrderId(orderId + 1);
        assertSoftly(softly -> {
            softly.assertThat(orderCoupons).hasSize(0);
        });
    }
}
