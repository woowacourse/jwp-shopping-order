package com.woowahan.techcourse.order.db;


import com.woowahan.techcourse.order.domain.OrderCoupon;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class OrderCouponDao {

    private static final RowMapper<OrderCoupon> ROW_MAPPER = (rs, rowNum) -> new OrderCoupon(rs.getLong("id"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_coupon")
                .usingGeneratedKeyColumns("id");
    }

    public void insertAll(long orderId, List<Long> couponIds) {
        for (Long couponId : couponIds) {
            insert(orderId, couponId);
        }
    }

    private long insert(long orderId, Long couponId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("order_id", orderId)
                .addValue("coupon_id", couponId);
        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    public List<OrderCoupon> findAllByOrderId(long orderId) {
        return jdbcTemplate.query("SELECT * FROM order_coupon WHERE order_id = ?", ROW_MAPPER, orderId);
    }
}
