package cart.step2.order.persist;

import cart.step2.order.domain.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderItemDao {

    private RowMapper<OrderItem> rowMapper = (rs, rowNum) ->
            OrderItem.of(
                    rs.getLong("id"),
                    rs.getLong("product_id"),
                    rs.getLong("order_id"),
                    rs.getInt("quantity")
            );
    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsert(final List<OrderItem> orderItems) {
        final String sql = "INSERT INTO order_item (product_id, order_id, quantity) " +
                "VALUES (?, ?, ?)";

        final List<Object[]> batchItems = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Object[] args = new Object[]{orderItem.getProductId(), orderItem.getOrderId(), orderItem.getQuantity()};
            batchItems.add(args);
        }

        jdbcTemplate.batchUpdate(sql, batchItems);
    }

    public List<OrderItem> findAll(final Long orderId) {
        final String sql = "SELECT * FROM order_item " +
                "WHERE order_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }

}
