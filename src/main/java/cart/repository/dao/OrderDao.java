package cart.repository.dao;

import cart.entity.OrderEntity;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertOrders;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertOrders = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public Long createOrder(final OrderEntity order) {
        Map<String, Object> params = new HashMap<>();
        params.put("member_id", order.getMemberId());
        params.put("original_price", order.getOriginalPrice());
        params.put("discounted_price", order.getDiscountedPrice());

        return insertOrders.executeAndReturnKey(params).longValue();
    }
}
