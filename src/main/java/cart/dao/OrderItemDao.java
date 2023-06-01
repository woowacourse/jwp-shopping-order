package cart.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cart.domain.CartItem;
import cart.domain.Order;

@Repository
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAllItems(Long orderId, Order order) {
        List<CartItem> cartItems = order.getCartItems();
        int itemCount = cartItems.size();

        String query = "INSERT INTO order_item (order_id, product_id, `name`, price, image_url, quantity) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CartItem cartItem = cartItems.get(i);
                Long productId = cartItem.getProduct().getId();
                String name = cartItem.getProduct().getName();
                int price = cartItem.getProduct().getPrice();
                String imageUrl = cartItem.getProduct().getImageUrl();
                int quantity = cartItem.getQuantity();

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

        int rowCount = jdbcTemplate.queryForObject("SELECT COUNT(id) FROM order_item WHERE order_id = ?", Integer.class, orderId);

        if (rowCount != itemCount) {
            throw new IllegalStateException("Batch insertion failed to insert all items.");
        }
    }
}
