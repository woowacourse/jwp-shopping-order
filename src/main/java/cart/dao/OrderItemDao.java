package cart.dao;

import cart.domain.OrderItem;
import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<OrderItem> rowMapper = (rs, rowNum) -> {
        long productId = rs.getLong("product_id");
        String productName = rs.getString("name");
        long productPrice = rs.getLong("price");
        String productImageUrl = rs.getString("image_url");
        return new OrderItem(
                rs.getLong("id"),
                new Product(productId, productName, productPrice, productImageUrl),
                rs.getLong("quantity")
        );
    };

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

    public List<OrderItem> findAllByOrderId(final long orderId) {
        String sql = "SELECT * FROM order_item WHERE orders_id = ? ";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
