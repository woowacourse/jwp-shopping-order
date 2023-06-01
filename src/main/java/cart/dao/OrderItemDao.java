package cart.dao;

import cart.domain.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }


    public Long insert(final Long orderId, final OrderItem orderItem) {
        final Map<String, Object> params = new HashMap<>();
        params.put("order_id", orderId);
        params.put("product_id", orderItem.getProductId());
        params.put("name", orderItem.getName());
        params.put("price", orderItem.getPrice());
        params.put("image_url", orderItem.getImage());
        params.put("quantity", orderItem.getQuantity());

        return insertAction.executeAndReturnKey(params).longValue();
    }
}
