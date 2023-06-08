package cart.persistence.order;

import cart.domain.order.OrderItem;
import cart.domain.order.OrderItemRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcTemplateOrderItemDao implements OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateOrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<OrderItem> findOrderItemsByOrderId(Long orderId) {
        String sql = "SELECT order_detail.id, order_detail.product_id, order_detail.product_name, order_detail.product_price, order_detail.product_image_url, order_detail.order_quantity " +
                "FROM order_detail " +
                "INNER JOIN orders ON orders.id = order_detail.orders_id " +
                "WHERE order_detail.orders_id = ?";
        return jdbcTemplate.query(sql, new Object[]{orderId}, (rs, rowNum) -> {
            Long id = rs.getLong("order_detail.id");
            Long productId = rs.getLong("order_detail.product_id");
            String productName = rs.getString("order_detail.product_name");
            Long productPrice = rs.getLong("order_detail.product_price");
            String productImageUrl = rs.getString("order_detail.product_image_url");
            Long orderQuantity = rs.getLong("order_detail.order_quantity");
            return new OrderItem(id, productId, productName, productPrice, productImageUrl, orderQuantity);
        });
    }

    @Override
    public void createOrderItem(Long orderId, OrderItem orderItem) {
        String sql = "INSERT INTO order_detail (orders_id, product_id, product_name, product_price, product_image_url, order_quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, orderId, orderItem.getProductId(), orderItem.getName(), orderItem.getPrice(), orderItem.getImageUrl(), orderItem.getQuantity());
    }
}