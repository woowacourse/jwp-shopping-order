package cart.persistence.order;

import cart.application.repository.order.OrderedItemRepository;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class OrderedItemJdbcRepository implements OrderedItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderedItemJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<OrderItem> orderItemRowMapper = ((rs, rowNum) ->
            new OrderItem(
                    rs.getLong("id"),
                    rs.getString("product_name"),
                    rs.getString("product_image"),
                    rs.getInt("product_price"),
                    rs.getInt("product_quantity")
            ));

    @Override
    public void createOrderItems(final Long orderId, final OrderItems orderItems) {
        String sql = "INSERT INTO ordered_item (order_id, product_name, product_price, product_quantity, product_image) VALUES (?,?,?,?,?);";
        jdbcTemplate.batchUpdate(sql, orderItems.getOrderItems(), orderItems.getOrderItems().size(),
                (PreparedStatement ps, OrderItem orderItem) -> {
                    ps.setLong(1, orderId);
                    ps.setString(2, orderItem.getProductName());
                    ps.setInt(3, orderItem.getProductPrice());
                    ps.setInt(4, orderItem.getProductQuantity());
                    ps.setString(5, orderItem.getProductImage());
                });
    }

    @Override
    public OrderItems findOrderItemsByOrderId(final Long orderId) {
        String sql = "SELECT id, order_id, product_name, product_price, product_quantity, product_image FROM ordered_item WHERE order_id = ?";
        return new OrderItems(jdbcTemplate.query(sql, orderItemRowMapper, orderId));
    }

}
