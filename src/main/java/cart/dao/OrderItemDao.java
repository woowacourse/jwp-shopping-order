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
        String sql = "INSERT INTO order_items (order_id, product_name, product_price, product_image_url, product_quantity) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                OrderItem orderItem = orderItems.get(i);
                Product product = orderItem.getProduct();
                ps.setLong(1, orderId);
                ps.setString(2, product.getName());
                ps.setInt(3, product.getPrice());
                ps.setString(4, product.getImageUrl());
                ps.setInt(5, orderItem.getQuantity());
                
            }
            
            @Override
            public int getBatchSize() {
                return orderItems.size();
            }
        });
    }
    
    
}
