package cart.dao;

import cart.entity.OrderItemEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<OrderItemEntity> rowMapper = (rs, rowNum) -> {
        final Long id = rs.getLong("id");
        final String name = rs.getString("name");
        final Long price = rs.getLong("price");
        final String imageUrl = rs.getString("image_url");
        final int quantity = rs.getInt("quantity");
        final long orderId = rs.getLong("order_id");

        return new OrderItemEntity(id, name, price, imageUrl, quantity, orderId);
    };

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingColumns("name", "price", "image_url", "quantity", "order_id")
                .usingGeneratedKeyColumns("id");
    }

    public OrderItemEntity insert(final OrderItemEntity orderItemEntity) {
        final BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(
                orderItemEntity);
        final long id = jdbcInsert.executeAndReturnKey(parameterSource).longValue();

        return new OrderItemEntity(
                id,
                orderItemEntity.getName(),
                orderItemEntity.getPrice(),
                orderItemEntity.getImageUrl(),
                orderItemEntity.getQuantity(),
                orderItemEntity.getOrderId()
        );
    }

    public List<OrderItemEntity> findAllByOrderId(final Long orderId) {
        String sql = "SELECT * FROM order_item WHERE order_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
