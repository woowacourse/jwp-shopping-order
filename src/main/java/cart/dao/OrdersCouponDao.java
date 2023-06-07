package cart.dao;

import cart.dao.entity.OrdersCouponEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrdersCouponDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<OrdersCouponEntity> ordersCouponEntityRowMapper = (rs, rowNum) -> new OrdersCouponEntity(
            rs.getLong("id"),
            rs.getLong("orders_id"),
            rs.getLong("coupon_id")
    );

    public OrdersCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createOrderCoupon(final long ordersId, final long couponId) {
        final String sql = "INSERT INTO orders_coupon(orders_id,coupon_id) VALUES (?,?)";
        jdbcTemplate.update(sql, ordersId, couponId);
    }

    public List<OrdersCouponEntity> finAllByOrdersId(final long ordersId) {
        final String sql = "SELECT * FROM orders_coupon WHERE orders_id = ?";
        return jdbcTemplate.query(sql, ordersCouponEntityRowMapper, ordersId);
    }
}
