package cart.dao;

import cart.dao.entity.OrderItemEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private static final RowMapper<OrderItemEntity> ROW_MAPPER = (resultSet, rowNum) -> new OrderItemEntity(
            resultSet.getLong("id"),
            resultSet.getLong("order_id"),
            resultSet.getString("name"),
            resultSet.getLong("price"),
            resultSet.getString("image_url"),
            resultSet.getInt("quantity")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id")
                .usingColumns("order_id", "name", "price", "image_url", "quantity");
    }

    public void saveAll(final List<OrderItemEntity> orderItems) {
        final BeanPropertySqlParameterSource[] parameterSources = orderItems.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(BeanPropertySqlParameterSource[]::new);
        simpleJdbcInsert.executeBatch(parameterSources);
    }

    public List<OrderItemEntity> findByOrderId(final Long orderId) {
        final String sql = "SELECT id, order_id, name, price, image_url, quantity "
                + "FROM order_item "
                + "WHERE order_id = ? ";
        return jdbcTemplate.query(sql, ROW_MAPPER, orderId);
    }

    public void deleteByOrderId(final Long orderId) {
        final String sql = "DELETE FROM order_item WHERE order_id = ? ";
        jdbcTemplate.update(sql, orderId);
    }
}
