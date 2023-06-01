package cart.dao.order;

import cart.domain.order.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcTemplateOrderItemDao implements OrderItemDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateOrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<OrderItem> findOrderItemsByOrderId(Long orderId) {
        String sql = "SELECT od.id, od.product_id, od.product_name, od.product_price, od.product_image_url, od.order_quantity " +
                "FROM order_detail as od " +
                "INNER JOIN orders as o ON o.id = od.orders_id " +
                "WHERE od.orders_id = ?";
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
