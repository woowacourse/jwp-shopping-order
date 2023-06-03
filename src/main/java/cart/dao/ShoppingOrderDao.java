package cart.dao;

import cart.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ShoppingOrderDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<Order> orderRowMapper = (rs, rowNum) -> {
        Long orderId = rs.getLong("id");
        LocalDateTime orderedAt = rs.getTimestamp("ordered_at").toLocalDateTime();
        Long usedPoint = rs.getLong("used_point");

        Long orderItemId = rs.getLong("orderitem_id");
        Integer quantity = rs.getInt("quantity");
        Long productId = rs.getLong("product_id");
        String productName = rs.getString("name");
        Integer productPrice = rs.getInt("price");
        String productImageUrl = rs.getString("image_url");
        OrderedItem orderedItem = new OrderedItem(orderItemId, new Product(productId, productName, new Price(productPrice), productImageUrl), quantity);
        System.out.println(orderedItem);

        return new Order(orderId, null, orderedAt, new Point(usedPoint), new OrderedItems(new ArrayList<>(List.of(orderedItem))));
    };

    public ShoppingOrderDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("shopping_order")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "ordered_at", "used_point");
    }

    public Long save(Order order) {

        Map<String, Object> params = new HashMap<>();
        params.put("member_id", order.getMember().getId());
        params.put("ordered_at", order.getOrderedAt());
        params.put("used_point", order.getUsedPoint().getValue());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<Order> findAll(Long id) {
        String sql = "SELECT so.id, so.ordered_at, so.used_point, oi.id as orderitem_id, oi.quantity, oi.product_id, p.name, p.price, p.image_url\n" +
                "FROM shopping_order so\n" +
                "JOIN ordered_item oi ON so.id = oi.order_id\n" +
                "JOIN product p ON oi.product_id = p.id\n" +
                "WHERE member_id = ?";
        return jdbcTemplate.query(sql, orderRowMapper, id);
    }

    public Long getSize() {
        String sql = "SELECT id FROM shopping_order";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public List<Order> findById(Long orderId) {
        String sql = "SELECT so.id, so.ordered_at, so.used_point, oi.id as orderitem_id, oi.quantity, oi.product_id, p.name, p.price, p.image_url\n" +
                "FROM ordered_item oi\n" +
                "JOIN shopping_order so ON oi.order_id = so.id\n" +
                "JOIN product p ON oi.product_id = p.id\n" +
                "WHERE so.id = ?";
        return jdbcTemplate.query(sql, orderRowMapper, orderId);
    }
}
