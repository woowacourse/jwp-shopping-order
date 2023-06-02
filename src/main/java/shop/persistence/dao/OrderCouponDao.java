package shop.persistence.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import shop.persistence.entity.detail.OrderCouponDetail;
import shop.persistence.entity.OrderCouponEntity;

import java.util.Optional;

@Component
public class OrderCouponDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<OrderCouponDetail> detailRowMapper =
            (rs, rowNum) -> new OrderCouponDetail(
                    rs.getLong("order_coupon.id"),
                    rs.getLong("order_coupon.order_id"),
                    rs.getLong("order_coupon.coupon_id"),
                    rs.getString("coupon.name"),
                    rs.getInt("coupon.discount_rate"),
                    rs.getInt("coupon.period"),
                    rs.getTimestamp("coupon.expiredAt").toLocalDateTime()
            );

    public OrderCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_coupon")
                .usingGeneratedKeyColumns();
    }

    public Long insert(OrderCouponEntity orderCouponEntity) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(orderCouponEntity);

        return simpleJdbcInsert.executeAndReturnKey(param).longValue();
    }

    public Optional<OrderCouponDetail> findCouponByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_coupon " +
                "INNER JOIN coupon ON order_coupon.coupon_id = coupon.id " +
                "WHERE order_coupon.order_id = ?";

        try {
            OrderCouponDetail orderCouponDetail = jdbcTemplate.queryForObject(sql, detailRowMapper, orderId);

            return Optional.of(orderCouponDetail);
        } catch (DataAccessException e) {
            return Optional.empty();
        }

    }
}
