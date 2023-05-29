package cart.dao;

import cart.entity.OrderItemEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

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
}
