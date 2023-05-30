package cart.dao;

import cart.domain.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // todo: long 반환?
    public void insert(final long orderId, final OrderItem orderItem) {
        String sql = "INSERT INTO order_item (name, price, image_url, quantity, product_id, orders_id) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                sql,
                orderItem.getProduct().getName(),
                orderItem.getProduct().getPrice(),
                orderItem.getProduct().getImageUrl(),
                orderItem.getQuantity(),
                orderItem.getProduct().getId(),
                orderId
        );
    }
}
