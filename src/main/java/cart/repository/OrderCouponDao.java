package cart.repository;

import cart.entity.OrderCouponEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCouponDao {

    private final static RowMapper<OrderCouponEntity> rowMapper = (rs, rowNum) ->
            new OrderCouponEntity(
                    rs.getLong("id"),
                    rs.getLong("order_item_id"),
                    rs.getLong("coupon_id")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(OrderCouponEntity orderCouponEntity) {
        final SqlParameterSource source = new BeanPropertySqlParameterSource(orderCouponEntity);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }

    public List<OrderCouponEntity> findByOrderId(Long orderId) {
        String sql =
                "select distinct order_coupon.id, order_coupon.order_item_id, order_coupon.coupon_id, order_item.order_id " +
                        "from order_coupon " +
                        "join order_item on order_coupon.order_item_id = order_item_id " +
                        "where order_item.order_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
