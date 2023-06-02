package cart.dao;

import cart.domain.OrderItem;
import cart.domain.Product;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {
    private static final RowMapper<OrderItem> mapper = (rs, rowNum) -> {
        long product_id = rs.getLong("product_id");
        int quantity = rs.getInt("quantity");
        String original_name = rs.getString("original_name");
        int original_price = rs.getInt("original_price");
        String original_image_url = rs.getString("original_image_url");
        int original_stock = rs.getInt("original_stock");

        Product originalProduct = new Product(product_id, original_name, original_price, original_image_url,
                original_stock);
        return new OrderItem(product_id, originalProduct, quantity);
    };
    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAll(Long orderId, List<OrderItem> orderItems) {
        String sql =
                "INSERT INTO order_item (orders_id, product_id, quantity, original_name, original_price, original_image_url, original_stock) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, orderId);
                Long productId = orderItems.get(i).getProductId();
                preparedStatement.setLong(2, productId);
                preparedStatement.setLong(3, orderItems.get(i).getQuantity());

                Product originalProduct = orderItems.get(i).getOriginalProduct();
                preparedStatement.setString(4, originalProduct.getName());
                preparedStatement.setInt(5, originalProduct.getPrice());
                preparedStatement.setString(6, originalProduct.getImageUrl());
                preparedStatement.setInt(7, originalProduct.getStock());
            }

            @Override
            public int getBatchSize() {
                return orderItems.size();
            }
        });
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_item WHERE orders_id = ?";
        return jdbcTemplate.query(sql, mapper, orderId);
    }
}
