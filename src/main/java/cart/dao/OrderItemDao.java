package cart.dao;

import cart.domain.OrderItem;
import cart.domain.Product;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<OrderItem> rowMapper = (rs, rowNum) -> {
        long orderItemId = rs.getLong("order_item.id");
        int orderItemQuantity = rs.getInt("order_item.quantity");
        long orderItemPrice = rs.getLong("order_item.price");
        long productId = rs.getLong("product.id");
        String productName = rs.getString("product.name");
        String imageUrl = rs.getString("product.image_url");
        long productPrice = rs.getLong("product.price");

        Product product = new Product(productId, productName, productPrice, imageUrl);
        return new OrderItem(orderItemId, product, orderItemQuantity, orderItemPrice);
    };

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Long orderId, OrderItem orderItem) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("order_id", orderId);
        mapSqlParameterSource.addValue("product_id", orderItem.getProduct().getId());
        mapSqlParameterSource.addValue("quantity", orderItem.getQuantity().getAmount());
        mapSqlParameterSource.addValue("price", orderItem.getPrice().getAmount());
        return simpleJdbcInsert.executeAndReturnKey(mapSqlParameterSource).longValue();
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        String sql = "SELECT o.id, o.quantity, o.price, p.id, p.name, p.image_url, p.price " +
                "FROM order_item AS o " +
                "INNER JOIN product AS p ON p.id = o.product_id " +
                "WHERE o.order_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
