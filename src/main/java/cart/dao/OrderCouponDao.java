package cart.dao;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderCouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderCouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("order_coupon");
    }

    private static RowMapper<Coupon> rowMapper() {
        return (rs, rowNum) -> new Coupon(
                rs.getLong("id"),
                rs.getString("name"),
                new Discount(rs.getString("discount_type"), rs.getInt("amount"))
        );
    }

    public void create(Long orderItemId, Long couponId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("order_item_id", orderItemId)
                .addValue("coupon_id", couponId);

        simpleJdbcInsert.execute(params);
    }

    public List<Coupon> findCouponsByOrderItemId(Long orderItemId) {
        String sql = "select coupon.* from order_coupon, coupon where order_coupon.coupon_id = coupon.id and order_item_id = ?";

        return jdbcTemplate.query(sql, rowMapper(), orderItemId);
    }
}
