package cart.db.dao;

import cart.db.entity.OrderCouponDetailEntity;
import cart.db.entity.OrderCouponEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderCouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public OrderCouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_coupon")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public void create(final OrderCouponEntity orderCouponEntity) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(orderCouponEntity);
        simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public OrderCouponDetailEntity findByOrderId(final Long orderId) {
        String sql = "SELECT order_coupon.id, order_id, " +
                "coupon_id, coupon.name, discount_rate, period, expired_at " +
                "FROM order_coupon JOIN coupon ON order_coupon.coupon_id = coupon.id " +
                "WHERE order_id = ?";
        return jdbcTemplate.queryForObject(sql, new OrderCouponDetailEntityRowMapper(), orderId);
    }

    public List<OrderCouponDetailEntity> findByOrderIds(final List<Long> orderIds) {
        String sql = "SELECT order_coupon.id, order_id, " +
                "coupon_id, coupon.name, discount_rate, period, expired_at " +
                "FROM order_coupon JOIN coupon ON order_coupon.coupon_id = coupon.id " +
                "WHERE order_id IN (:ids)";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("ids", orderIds);
        return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, new OrderCouponDetailEntityRowMapper());
    }

    private static class OrderCouponDetailEntityRowMapper implements RowMapper<OrderCouponDetailEntity> {
        @Override
        public OrderCouponDetailEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new OrderCouponDetailEntity(
                    rs.getLong("order_coupon.id"),
                    rs.getLong("order_id"),
                    rs.getLong("coupon_id"),
                    rs.getString("coupon.name"),
                    rs.getInt("discount_rate"),
                    rs.getInt("period"),
                    rs.getTimestamp("expired_at").toLocalDateTime()
            );
        }
    }
}
