package com.woowahan.techcourse.order.db;


import com.woowahan.techcourse.order.domain.OrderCoupon;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class OrderCouponDao {

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

    private void insert(long orderId, Long couponId) {
        simpleJdbcInsert.execute(orderCouponToMap(orderId, couponId));
    }

    private Map<String, Object> orderCouponToMap(long orderId, Long couponId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("order_id", orderId);
        parameters.put("coupon_id", couponId);
        return parameters;
    }

    public List<OrderCoupon> findAllByOrderId(long orderId) {
        return jdbcTemplate.query("SELECT * FROM order_coupon WHERE order_id = ?",
                (rs, rowNum) -> new OrderCoupon(rs.getLong("id")), orderId);
    }
}
