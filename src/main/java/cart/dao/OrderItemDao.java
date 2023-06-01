package cart.dao;

import cart.domain.OrderItem;
import cart.domain.Product;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAll(Long orderId, List<OrderItem> orderItems) {
        String sql = "INSERT INTO order_item (orders_id, product_id, quantity, original_name, original_price, original_image_url)"
                + "VALUES (?, ?, ?, ?, ?, ?)";
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
            }

            @Override
            public int getBatchSize() {
                return orderItems.size();
            }
        });
    }
}
