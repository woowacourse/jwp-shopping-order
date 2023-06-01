package cart.dao;

import cart.domain.OrderItem;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<OrderItem> rowMapper = (rs, rowNum) -> new OrderItem(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("image_url"),
            rs.getInt("quantity"),
            rs.getInt("discount_rate")
    ) ;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(final List<OrderItem> orderItems, final Long orderId) {
        String sql = "INSERT INTO order_item(name, price, image_url, quantity, discount_rate, order_id) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, orderItems.get(i).getName());
                ps.setInt(2, orderItems.get(i).getPrice());
                ps.setString(3, orderItems.get(i).getImageUrl());
                ps.setInt(4, orderItems.get(i).getQuantity());
                ps.setInt(5, orderItems.get(i).getDiscountRate());
                ps.setLong(6, orderId);
            }

            @Override
            public int getBatchSize() {
                return orderItems.size();
            }
        });
    }

    public List<OrderItem> findByOrderId(final Long orderId) {
        String sql = "SELECT id, name, price, image_url, quantity, discount_rate FROM order_item " +
                "WHERE order_id = ?";

        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
