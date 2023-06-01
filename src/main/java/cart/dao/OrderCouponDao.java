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

    public void saveOrderCoupon(Long orderId, Long couponId) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(new OrderCouponEntity(orderId, couponId));
        insertAction.executeAndReturnKey(params).longValue();
    }
}
