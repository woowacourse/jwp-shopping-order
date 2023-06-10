package cart.dao;

import cart.domain.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<OrderItem> rowMapper = (rs, rowNum) ->
            new OrderItem(
                    rs.getLong("product_id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("image_url"),
                    rs.getInt("quantity")
            );

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }


    public Long insert(final Long orderId, final OrderItem orderItem) {
        final Map<String, Object> params = new HashMap<>();
        params.put("order_id", orderId);
        params.put("product_id", orderItem.getProductId());
        params.put("name", orderItem.getName());
        params.put("price", orderItem.getPrice());
        params.put("image_url", orderItem.getImage());
        params.put("quantity", orderItem.getQuantity());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<OrderItem> findByOrderId(final Long orderId) {
        final String sql = "select product_id, name, price, image_url, quantity from order_item WHERE order_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
