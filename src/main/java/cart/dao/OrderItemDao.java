package cart.dao;

import cart.entity.OrderItemEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {
    private static final RowMapper<OrderItemEntity> MAPPER = (resultSet, rowNum) -> new OrderItemEntity(
            resultSet.getLong("id"),
            resultSet.getLong("order_id"),
            resultSet.getLong("product_id"),
            resultSet.getInt("quantity")
    );

    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAll(final List<OrderItemEntity> orderItemEntities) {
        final String sql = "INSERT INTO order_item (order_id, product_id, quantity) values (?,?,?)";
        jdbcTemplate.batchUpdate(sql, orderItemEntities, orderItemEntities.size(), ((ps, orderItemEntity) -> {
            ps.setLong(1, orderItemEntity.getOrderId());
            ps.setLong(2, orderItemEntity.getProductId());
            ps.setInt(3, orderItemEntity.getQuantity());
        }));
    }

    public List<OrderItemEntity> findByOrderId(final Long orderId) {
        final String sql = "SELECT * FROM order_item where order_id = ?";
        return jdbcTemplate.query(sql, MAPPER, orderId);
    }
}
