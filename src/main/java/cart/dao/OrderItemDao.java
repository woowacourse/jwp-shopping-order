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
        long productId = rs.getLong("p_id");
        String productName = rs.getString("name");
        long productPrice = rs.getLong("p_price");
        String imageUrl = rs.getString("image_url");
        Product product = new Product(productId, productName, productPrice, imageUrl);

        long orderItemId = rs.getLong("oi_id");
        int orderItemQuantity = rs.getInt("quantity");
        long orderItemPrice = rs.getLong("oi_price");
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
        String sql = "SELECT oi.id AS oi_id, oi.quantity, oi.price AS oi_price, p.id AS p_id, p.name, p.image_url, p.price AS p_price " +
                "FROM order_item AS oi " +
                "INNER JOIN product AS p ON p.id = oi.product_id " +
                "WHERE oi.order_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
