package cart.repository.dao;

import cart.entity.OrderItemEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert orderItemInsert;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderItemInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public void saveAll(List<OrderItemEntity> orderItems) {
        List<Map<String, Object>> batchOrderItems = new ArrayList<>();

        for (OrderItemEntity orderItem : orderItems) {
            Map<String, Object> params = new HashMap<>();
            params.put("orders_id", orderItem.getOrdersId());
            params.put("product_id", orderItem.getProductId());
            params.put("quantity", orderItem.getQuantity());

            batchOrderItems.add(params);
        }

        orderItemInsert.executeBatch(batchOrderItems.toArray(new Map[0]));
    }

    public List<OrderItemEntity> getOrderItemsByOrderId(Long orderId) {
        String sql = "SELECT id, orders_id product_id, quantity FROM order_item where orders_id = ?";
        return jdbcTemplate.query(sql, new Object[]{orderId}, orderItemRowMapper);
    }

    private final RowMapper<OrderItemEntity> orderItemRowMapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        Long orderId = rs.getLong("orders_id");
        Long productId = rs.getLong("product_id");
        int quantity = rs.getInt("quantity");
        return new OrderItemEntity(id, orderId, productId, quantity);
    };
}
