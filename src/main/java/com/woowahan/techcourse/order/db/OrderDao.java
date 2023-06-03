package com.woowahan.techcourse.order.db;

import com.woowahan.techcourse.order.domain.Order;
import com.woowahan.techcourse.order.domain.OrderCoupon;
import com.woowahan.techcourse.order.domain.OrderItem;
import com.woowahan.techcourse.order.domain.OrderResult;
import com.woowahan.techcourse.order.exception.OrderNotFoundException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final OrderCouponDao orderCouponDao;
    private final OrderItemDao orderItemDao;


    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "original_price", "actual_price");
        orderCouponDao = new OrderCouponDao(jdbcTemplate);
        orderItemDao = new OrderItemDao(jdbcTemplate);
    }

    public Long insert(OrderResult orderResult) {
        MapSqlParameterSource parameters = orderResultToParameters(orderResult);
        long orderId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        orderCouponDao.insertAll(orderId, orderResult.getOrder().getCouponIds());
        orderItemDao.insertAll(orderId, orderResult.getOrder().getOrderItems());
        return orderId;
    }

    private MapSqlParameterSource orderResultToParameters(OrderResult orderResult) {
        return new MapSqlParameterSource()
                .addValue("member_id", orderResult.getOrder().getMemberId())
                .addValue("original_price", orderResult.getOriginalPrice())
                .addValue("actual_price", orderResult.getActualPrice());
    }

    public OrderResult findById(long orderId) {
        try {
            return findOrderResultById(orderId);
        } catch (Exception e) {
            throw new OrderNotFoundException();
        }
    }

    public List<OrderResult> findAllByMemberId(long memberId) {
        return jdbcTemplate.query("SELECT id FROM orders WHERE member_id = ?",
                (rs, rowNum) -> findById(rs.getLong("id")), memberId);
    }

    private OrderResult findOrderResultById(long orderId) {
        List<OrderCoupon> orderCoupons = orderCouponDao.findAllByOrderId(orderId);
        List<OrderItem> orderItems = orderItemDao.findAllByOrderId(orderId);

        return jdbcTemplate.queryForObject(
                "SELECT member_id, id, original_price, actual_price FROM orders WHERE id = ?",
                getOrderResultRowMapper(orderCoupons, orderItems), orderId);
    }

    private RowMapper<OrderResult> getOrderResultRowMapper(List<OrderCoupon> orderCoupons,
            List<OrderItem> orderItems) {
        return (rs, rowNum) -> new OrderResult(
                rs.getInt("original_price"),
                rs.getInt("actual_price"), new Order(
                rs.getLong("member_id"),
                rs.getLong("id"),
                orderItems,
                orderCoupons
        ));
    }
}
