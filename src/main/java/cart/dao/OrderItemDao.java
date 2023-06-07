package cart.dao;

import cart.domain.order.OrderItem;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class OrderItemDao {

    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("order_items")
            .usingGeneratedKeyColumns("id");
    }

    public OrderItem insert(final Long orderId, final OrderItem orderItem) {
        final MapSqlParameterSource params = generateOrderParamMap(orderId, orderItem);

        final long orderItemId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return OrderItem.persisted(orderItemId, orderItem.getProduct(), orderItem.getQuantity());
    }

    private MapSqlParameterSource generateOrderParamMap(final Long orderId, final OrderItem orderItem) {
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("order_id", orderId);
        params.addValue("product_name", orderItem.getProduct().getName());
        params.addValue("product_price", orderItem.getProduct().getPrice());
        params.addValue("product_image_url", orderItem.getProduct().getImageUrl());
        params.addValue("product_quantity", orderItem.getQuantity());
        return params;
    }

    public void insertAll(final Long orderId, final List<OrderItem> orderItems) {
        final MapSqlParameterSource[] paramMaps = orderItems.stream()
            .map(orderItem -> generateOrderParamMap(orderId, orderItem))
            .collect(Collectors.toList())
            .toArray(new MapSqlParameterSource[orderItems.size()]);
        simpleJdbcInsert.executeBatch(paramMaps);
    }
}
