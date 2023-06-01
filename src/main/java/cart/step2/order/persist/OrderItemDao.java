package cart.step2.order.persist;

import cart.step2.order.domain.OrderItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderItemDao {

    private RowMapper<OrderItemEntity> rowMapper = (rs, rowNum) ->
            OrderItemEntity.of(
                    rs.getLong("id"),
                    rs.getLong("product_id"),
                    rs.getLong("order_id"),
                    rs.getInt("quantity")
            );
    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsert(final List<OrderItemEntity> orderItemEntities) {
        final String sql = "INSERT INTO order_item (product_id, order_id, quantity) " +
                "VALUES (?, ?, ?)";

        final List<Object[]> batchItems = new ArrayList<>();
        for (OrderItemEntity orderItem : orderItemEntities) {
            Object[] args = new Object[]{orderItem.getProductId(), orderItem.getOrderId(), orderItem.getQuantity()};
            batchItems.add(args);
        }

        jdbcTemplate.batchUpdate(sql, batchItems);
    }

    public List<OrderItemEntity> findAll(final Long orderId) {
        final String sql = "SELECT * FROM order_item " +
                "WHERE order_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }

    public List<OrderItemEntity> findByOrderId(final Long orderId) {
        final String sql = "SELECT * FROM order_item " +
                "WHERE order_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }

}
