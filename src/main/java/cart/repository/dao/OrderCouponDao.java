package cart.repository.dao;

import cart.repository.entity.OrderCouponEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderCouponDao {

    private static final String SELECT_SQL = "SELECT id, coupon_name, discount_amount, member_coupon_id, orders_id FROM orders_coupon ";
    private static final String WHERE_ORDER_ID = "WHERE orders_id = ? ";

    private static final RowMapper<OrderCouponEntity> ORDER_COUPON_ROW_MAPPER = ((rs, rowNum) ->
            new OrderCouponEntity(
                    rs.getLong("id"),
                    rs.getString("coupon_name"),
                    rs.getInt("discount_amount"),
                    rs.getLong("member_coupon_id"),
                    rs.getLong("orders_id")
            )
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public OrderCouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders_coupon")
                .usingGeneratedKeyColumns("id");
    }

    public void insertAll(List<OrderCouponEntity> coupons) {
        MapSqlParameterSource[] sources = coupons.stream()
                .map(coupon -> new MapSqlParameterSource()
                        .addValue("coupon_name", coupon.getCouponName())
                        .addValue("discount_amount", coupon.getDiscountAmount())
                        .addValue("member_coupon_id", coupon.getMemberCouponId())
                        .addValue("orders_id", coupon.getOrderId())
                ).toArray(MapSqlParameterSource[]::new);

        jdbcInsert.executeBatch(sources);
    }

    public List<OrderCouponEntity> findAllByOrderId(Long orderId) {
        String sql = SELECT_SQL + WHERE_ORDER_ID;
        return jdbcTemplate.query(sql, ORDER_COUPON_ROW_MAPPER, orderId);
    }
}
