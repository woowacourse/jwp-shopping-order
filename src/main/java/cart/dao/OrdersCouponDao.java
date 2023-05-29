package cart.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrdersCouponDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    public OrdersCouponDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders_coupon")
                .usingGeneratedKeyColumns("id");
    }
    public void createOrderCoupon(final long ordersId, final long couponId){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("orders_id",ordersId)
                .addValue("coupon_id",couponId);
        simpleJdbcInsert.execute(parameterSource);
    }
}
