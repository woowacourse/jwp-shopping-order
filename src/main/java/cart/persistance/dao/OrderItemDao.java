package cart.persistance.dao;

import cart.persistance.entity.OrderItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderItemDao {
    private static final String SELECT_SQL = "SELECT id, product_name, price, image_url, quantity, product_id, orders_id FROM orders_item ";
    private static final String WHERE_ORDERS_ID = "where orders_id = ? ";

    private static final RowMapper<OrderItemEntity> ORDER_ITEM_ROW_MAPPER = ((rs, rowNum) ->
            new OrderItemEntity(
                    rs.getLong("id"),
                    rs.getString("product_name"),
                    rs.getInt("price"),
                    rs.getString("image_url"),
                    rs.getInt("quantity"),
                    rs.getLong("product_id"),
                    rs.getLong("orders_id")
            )
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders_item")
                .usingGeneratedKeyColumns("id");
    }

    public void insertAll(List<OrderItemEntity> orderItemEntities, Long orderId) {
        MapSqlParameterSource[] sources = orderItemEntities.stream()
                .map(orderItem -> {
                    return new MapSqlParameterSource()
                            .addValue("product_name", orderItem.getProductName())
                            .addValue("price", orderItem.getPrice())
                            .addValue("image_url", orderItem.getImageUrl())
                            .addValue("quantity", orderItem.getQuantity())
                            .addValue("product_id", orderItem.getProductId())
                            .addValue("orders_id", orderId);
                }).toArray(MapSqlParameterSource[]::new);

        simpleJdbcInsert.executeBatch(sources);
    }

    public List<OrderItemEntity> findAllByOrderId(Long orderId) {
        String sql = SELECT_SQL + WHERE_ORDERS_ID;
        return jdbcTemplate.query(sql, ORDER_ITEM_ROW_MAPPER, orderId);
    }
}
