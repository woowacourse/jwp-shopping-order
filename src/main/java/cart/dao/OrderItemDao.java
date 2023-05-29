package cart.dao;

import cart.domain.order.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("order_item")
            .usingGeneratedKeyColumns("id");
    }

    public OrderItem insert(final Long orderId, final OrderItem orderItem) {
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("order_id", orderId);
        params.addValue("product_id", orderItem.getProduct().getId());
        params.addValue("quantity", orderItem.getQuantity());

        final long orderItemId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return OrderItem.persisted(orderItemId, orderItem.getProduct(), orderItem.getQuantity());
    }
}
