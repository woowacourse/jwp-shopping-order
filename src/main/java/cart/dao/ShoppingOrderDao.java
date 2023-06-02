package cart.dao;

import cart.domain.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public class ShoppingOrderDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<OrderResponseEntity> detailOrderResponseEntityRowMapper = (rs, rowNum) -> {
        Long orderId = rs.getLong("id");
        LocalDateTime orderedAt = rs.getTimestamp("ordered_at").toLocalDateTime();
        Long usedPoint = rs.getLong("used_point");
        Long orderItemId = rs.getLong("orderitem_id");
        Integer quantity = rs.getInt("quantity");
        Long productId = rs.getLong("product_id");
        String productName = rs.getString("name");
        Integer productPrice = rs.getInt("price");
        String productImageUrl = rs.getString("image_url");

        return new OrderResponseEntity(orderId, orderedAt, usedPoint, orderItemId, quantity, productId, productName, productPrice, productImageUrl);
    };

    public ShoppingOrderDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("shopping_order")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO shopping_order (member_id, ordered_at, used_point) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, order.getMember().getId());
            ps.setTimestamp(2, Timestamp.valueOf(order.getOrderedAt()));
            ps.setLong(3, order.getUsedPoint().getValue());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<OrderResponseEntity> findAll(Long id) {
        String sql = "SELECT so.id, so.ordered_at, so.used_point, oi.id as orderitem_id, oi.quantity, oi.product_id, p.name, p.price, p.image_url\n" +
                "FROM ordered_item oi\n" +
                "JOIN shopping_order so ON oi.order_id = so.id\n" +
                "JOIN product p ON oi.product_id = p.id\n" +
                "WHERE member_id = ?";
        return jdbcTemplate.query(sql, detailOrderResponseEntityRowMapper, id);
    }

    public Long getSize() {
        String sql = "SELECT id FROM shopping_order";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public List<OrderResponseEntity> findById(Long orderId) {
        String sql = "SELECT so.id, so.ordered_at, so.used_point, oi.id as orderitem_id, oi.quantity, oi.product_id, p.name, p.price, p.image_url\n" +
                "FROM ordered_item oi\n" +
                "JOIN shopping_order so ON oi.order_id = so.id\n" +
                "JOIN product p ON oi.product_id = p.id\n" +
                "WHERE so.id = ?";
        return jdbcTemplate.query(sql, detailOrderResponseEntityRowMapper, orderId);
    }
}
