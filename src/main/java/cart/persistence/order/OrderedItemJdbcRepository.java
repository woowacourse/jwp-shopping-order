package cart.persistence.order;

import cart.application.repository.order.OrderedItemRepository;
import cart.domain.order.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class OrderedItemJdbcRepository implements OrderedItemRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderedItemJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("ordered_item")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<OrderItem> orderItemRowMapper = ((rs, rowNum) ->
            new OrderItem(
                    rs.getLong("id"),
                    rs.getLong("order_id"),
                    rs.getString("product_name"),
                    rs.getString("product_image"),
                    rs.getInt("product_price"),
                    rs.getInt("product_quantity")
            ));

    @Override
    public void createOrderItems(final List<OrderItem> orderItems) {
        String sql = "INSERT INTO ordered_item (order_id, product_name, product_price, product_quantity, product_image) VALUES (?,?,?,?,?);";
        jdbcTemplate.batchUpdate(sql, orderItems, orderItems.size(),
                (PreparedStatement ps, OrderItem orderItem) -> {
                    ps.setLong(1, orderItem.getOrderId());
                    ps.setString(2, orderItem.getProductName());
                    ps.setInt(3, orderItem.getProductPrice());
                    ps.setInt(4, orderItem.getProductQuantity());
                    ps.setString(5, orderItem.getProductImage());
                });
    }

    @Override
    public List<OrderItem> findOrderItemsByOrderId(final Long orderId) {
        String sql = "SELECT id, order_id, product_name, product_price, product_quantity, product_image FROM ordered_item WHERE order_id = ?";
        return jdbcTemplate.query(sql, orderItemRowMapper, orderId);
    }
}
