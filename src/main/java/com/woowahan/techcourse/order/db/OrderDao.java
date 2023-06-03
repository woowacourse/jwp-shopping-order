package com.woowahan.techcourse.order.db;

import com.woowahan.techcourse.order.domain.Order;
import com.woowahan.techcourse.order.domain.OrderCoupon;
import com.woowahan.techcourse.order.domain.OrderItem;
import com.woowahan.techcourse.order.exception.OrderNotFoundException;
import java.util.List;
import java.util.Optional;
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

    public Long insert(Order order) {
        MapSqlParameterSource parameters = orderToParameters(order);
        long orderId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        orderCouponDao.insertAll(orderId, order.getCouponIds());
        orderItemDao.insertAll(orderId, order.getOrderItems());
        return orderId;
    }

    private MapSqlParameterSource orderToParameters(Order order) {
        return new MapSqlParameterSource()
                .addValue("member_id", order.getMemberId())
                .addValue("original_price", order.getOriginalPrice())
                .addValue("actual_price", order.getActualPrice());
    }

    public Optional<Order> findById(long orderId) {
        try {
            return findOrderResultById(orderId);
        } catch (Exception e) {
            throw new OrderNotFoundException();
        }
    }

    public List<Order> findAllByMemberId(long memberId) {
        return jdbcTemplate.query("SELECT id FROM orders WHERE member_id = ?",
                (rs, rowNum) -> findOrderResultByIdAndGet(rs.getLong("id")), memberId);
    }

    private Order findOrderResultByIdAndGet(long orderId) {
        List<OrderCoupon> orderCoupons = orderCouponDao.findAllByOrderId(orderId);
        List<OrderItem> orderItems = orderItemDao.findAllByOrderId(orderId);
        return jdbcTemplate.queryForObject(
                "SELECT member_id, id, original_price, actual_price FROM orders WHERE id = ?",
                getOrderResultRowMapper(orderCoupons, orderItems), orderId);
    }

    private Optional<Order> findOrderResultById(long orderId) {
        List<OrderCoupon> orderCoupons = orderCouponDao.findAllByOrderId(orderId);
        List<OrderItem> orderItems = orderItemDao.findAllByOrderId(orderId);

        return executeFineQuery(orderId, orderCoupons, orderItems);
    }

    private Optional<Order> executeFineQuery(long orderId, List<OrderCoupon> orderCoupons, List<OrderItem> orderItems) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT member_id, id, original_price, actual_price FROM orders WHERE id = ?",
                    getOrderResultRowMapper(orderCoupons, orderItems), orderId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private RowMapper<Order> getOrderResultRowMapper(List<OrderCoupon> orderCoupons,
            List<OrderItem> orderItems) {
        return (rs, rowNum) -> new Order(
                rs.getLong("id"),
                rs.getLong("member_id"),
                orderItems,
                orderCoupons,
                rs.getBigDecimal("original_price"),
                rs.getBigDecimal("actual_price")
        );
    }
}
