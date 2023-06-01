package cart.dao;

import cart.domain.OrderItem;
import cart.domain.OrderItems;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAll(OrderItems orderItems, Long orderId) {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                OrderItem orderItem = orderItems.getItems().get(i);
                ps.setLong(1, orderId);
                ps.setLong(2, orderItem.getProduct().getId());
                ps.setInt(3, orderItem.getQuantity());
            }

            @Override
            public int getBatchSize() {
                return orderItems.getItems().size();
            }
        });
    }
}
