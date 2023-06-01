package cart.dao;

import cart.domain.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ShoppingOrderDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<OrderResponseEntity> orderResponseRowMapper = (rs, rowNum) -> {
        Long orderId = rs.getLong("id");
        LocalDateTime orderedAt = rs.getTimestamp("ordered_at").toLocalDateTime();
        Long orderItemId = rs.getLong("orderitem_id");
        Long quantity = rs.getLong("quantity");
        Long productId = rs.getLong("product_id");
        String productName = rs.getString("name");
        Long productPrice = rs.getLong("price");
        String productImageUrl = rs.getString("image_url");

        return new OrderResponseEntity(orderId, orderedAt, orderItemId, quantity, productId, productName, productPrice, productImageUrl);
    };

    public ShoppingOrderDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("shopping_order")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Order order) {
        Map<String, Object> params = new HashMap<>();
        params.put("member_id", order.getMember().getId());
        params.put("ordered_at", order.getOrderedAt());
        params.put("used_point", order.getUsedPoint().getValue());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<OrderResponseEntity> findAll(Long id) {
        String sql = "SELECT so.id, so.ordered_at, oi.id as orderitem_id, oi.quantity, oi.product_id, p.name, p.price, p.image_url\n" +
                "FROM ordered_item oi\n" +
                "JOIN shopping_order so ON oi.order_id = so.id\n" +
                "JOIN product p ON oi.product_id = p.id\n" +
                "WHERE member_id = ?";
        return jdbcTemplate.query(sql, orderResponseRowMapper, id);
    }

    public Long getSize() {
        String sql = "SELECT id FROM shopping_order";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
