package cart.dao;

import cart.entity.OrderCouponEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

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
}
