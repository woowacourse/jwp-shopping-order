package cart.dao;

import cart.domain.orderitem.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item").usingGeneratedKeyColumns("id");
    }

    public Long insert(OrderItem orderItem) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("cart_order_id", orderItem.getOrder().getId())
                .addValue("product_id", orderItem.getProductId())
                .addValue("name", orderItem.getName())
                .addValue("price", orderItem.getPrice())
                .addValue("image_url", orderItem.getImageUrl())
                .addValue("quantity", orderItem.getQuantity());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }
}
