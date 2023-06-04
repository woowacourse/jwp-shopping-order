package cart.infrastructure.dao;

import cart.entity.OrderEntity;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("`order`")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "coupon_id", "total_amount", "discounted_amount", "address",
                        "delivery_amount");
    }

    public Long create(final OrderEntity order, final Long memberId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("member_id", memberId);
        params.put("coupon_id", order.getCouponId());
        params.put("total_amount", order.getTotalAmount());
        params.put("discounted_amount", order.getDiscountedAmount());
        params.put("address", order.getAddress());
        params.put("delivery_amount", order.getDeliveryAmount());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }
}
