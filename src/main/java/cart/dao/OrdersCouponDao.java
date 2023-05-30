package cart.dao;

import cart.dao.entity.OrdersCouponEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrdersCouponDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<OrdersCouponEntity> ordersCouponEntityRowMapper = (rs, rowNum) -> new OrdersCouponEntity(
            rs.getLong("id"),
            rs.getLong("orders_id"),
            rs.getLong("coupon_id")
    );

    public OrdersCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders_coupon")
                .usingGeneratedKeyColumns("id");
    }

    public void createOrderCoupon(final long ordersId, final long couponId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("orders_id", ordersId)
                .addValue("coupon_id", couponId);
        simpleJdbcInsert.execute(parameterSource);
    }

    public List<OrdersCouponEntity> finAllByOrdersId(final long ordersId) {
        final String sql = "SELECT * FROM orders_coupon WHERE orders_id = ?";
        return jdbcTemplate.query(sql, ordersCouponEntityRowMapper, ordersId);
    }
}
