package cart.dao;

import cart.domain.Orders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrdersDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    public OrdersDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public Long createOrders(final Orders orders){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("member_id",orders.getMemberId())
                .addValue("original_price",orders.getOriginalPrice())
                .addValue("discount_price",orders.getDiscountPrice())
                .addValue("coupon_id", orders.getCouponId())
                .addValue("confirm_state",false);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }
}
