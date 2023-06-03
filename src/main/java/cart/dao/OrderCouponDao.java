package cart.dao;

import cart.entity.CouponEntity;
import cart.entity.OrderCouponEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OrderCouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public OrderCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_coupon")
                .usingGeneratedKeyColumns("id");
    }

    public void saveOrderCoupon(Long orderId, Long memberCouponId) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(new OrderCouponEntity(memberCouponId, orderId));
        insertAction.executeAndReturnKey(params).longValue();
    }

    public void deleteOrderCoupon(Long orderId) {
        String sql = "DELETE FROM order_coupon WHERE order_id = ?";
        jdbcTemplate.update(sql, orderId);
    }

    public Long findByOrderId(Long orderId) {
        String sql = "select member_coupon_id from order_coupon where order_id = ?";

        return jdbcTemplate.queryForObject(sql, Long.class, orderId);
    }

    public boolean checkOrderCouponByOrderId(Long orderId) {
        String sql = "select exists(select * from order_coupon where order_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, orderId);
    }

    public Optional<CouponEntity> findCouponByOrderId(Long orderId) {
        try {
            String sql = "SELECT * " +
                    "FROM order_coupon " +
                    "INNER JOIN member_coupon ON order_coupon.member_coupon_id = member_coupon.id " +
                    "INNER JOIN coupon ON member_coupon.coupon_id = coupon.id " +
                    "WHERE order_coupon.order_id = ?";

            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    new CouponEntity(
                            rs.getLong("order_coupon.id"),
                            rs.getString("coupon.name"),
                            rs.getString("coupon.discount_type"),
                            rs.getInt("coupon.minimum_price"),
                            rs.getInt("coupon.discount_price"),
                            rs.getDouble("coupon.discount_rate")
                    ), orderId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
