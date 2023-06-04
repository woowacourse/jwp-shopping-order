package cart.repository;

import cart.domain.product.OrderItem;
import cart.domain.product.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemRepository {

    private static final String SELECT_SQL = "SELECT id, product_name, price, image_url, quantity, product_id, orders_id FROM orders_item ";
    private static final String WHERE_ORDERS_ID = "where orders_id = ? ";

    private static final RowMapper<OrderItem> ORDER_ITEM_ROW_MAPPER = ((rs, rowNum) ->
            new OrderItem(
                    rs.getLong("id"),
                    rs.getInt("quantity"),
                    new Product(
                            rs.getLong("product_id"),
                            rs.getString("product_name"),
                            rs.getInt("price"),
                            rs.getString("image_url")
                    )
            )
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders_item")
                .usingGeneratedKeyColumns("id");
    }

    public void insertAll(List<OrderItem> orderItems, Long orderId) {
        MapSqlParameterSource[] sources = orderItems.stream()
                .map(orderItem -> {
                    Product product = orderItem.getProduct();
                    return new MapSqlParameterSource()
                            .addValue("product_name", product.getName())
                            .addValue("price", product.getPrice())
                            .addValue("image_url", product.getImageUrl())
                            .addValue("quantity", orderItem.getQuantity())
                            .addValue("product_id", product.getId())
                            .addValue("orders_id", orderId);
                }).toArray(MapSqlParameterSource[]::new);

        simpleJdbcInsert.executeBatch(sources);
    }

    public List<OrderItem> findAllByOrderId(Long orderId) {
        String sql = SELECT_SQL + WHERE_ORDERS_ID;
        return jdbcTemplate.query(sql, ORDER_ITEM_ROW_MAPPER, orderId);
    }
}
