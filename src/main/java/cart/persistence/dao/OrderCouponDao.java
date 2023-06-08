package cart.persistence.dao;

import cart.persistence.entity.OrderCouponEntity;
import java.sql.PreparedStatement;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCouponDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderCouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(final OrderCouponEntity orderCouponEntity) {
        final String query = "INSERT INTO order_coupon(order_id, coupon_id) VALUES (?, ?)";

        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
            ps.setLong(1, orderCouponEntity.getOrderId());
            ps.setLong(2, orderCouponEntity.getCouponId());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
