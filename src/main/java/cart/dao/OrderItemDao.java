package cart.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cart.domain.Order;
import cart.domain.OrderItem;

@Repository
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAllItems(Long orderId, Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        int itemCount = orderItems.size();

        String query = "INSERT INTO order_item (order_id, product_id, `name`, price, image_url, quantity) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                OrderItem orderItem = orderItems.get(i);
                Long productId = orderItem.getProductId();
                String name = orderItem.getName();
                int price = orderItem.getPrice();
                String imageUrl = orderItem.getImageUrl();
                int quantity = orderItem.getQuantity();

                ps.setLong(1, orderId);
                ps.setLong(2, productId);
                ps.setString(3, name);
                ps.setInt(4, price);
                ps.setString(5, imageUrl);
                ps.setInt(6, quantity);
            }

            @Override
            public int getBatchSize() {
                return itemCount;
            }
        });

        jdbcTemplate.queryForObject("SELECT COUNT(id) FROM order_item WHERE order_id = ?", Integer.class, orderId);
    }

    public List<OrderItem> getAllItems(Long orderId) {
        String query = "SELECT * FROM order_item WHERE order_id = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, orderId);
        return rows.stream().map(row -> {
                Long id = (Long)row.get("id");
                Long productId = (Long)row.get("product_id");
                String name = (String)row.get("name");
                int price = (int)row.get("price");
                String imageUrl = (String)row.get("image_url");
                int quantity = (int)row.get("quantity");

                return new OrderItem(id, orderId, productId, name, price, imageUrl, quantity);
            }
        ).collect(Collectors.toUnmodifiableList());
    }

    public void deleteAllOf(Long orderId) {
        String query = "DELETE FROM order_item WHERE order_id = ?";
        jdbcTemplate.update(query, orderId);
    }
}
