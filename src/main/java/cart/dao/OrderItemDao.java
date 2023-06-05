package cart.dao;

import cart.entity.OrderItemEntity;
import java.util.Collections;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<OrderItemEntity> rowMapper = (resultSet, rowNum) -> new OrderItemEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("image_url"),
            resultSet.getLong("price"),
            resultSet.getInt("quantity"),
            resultSet.getLong("order_id")
    );

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingColumns("name", "image_url", "price", "quantity", "order_id")
                .usingGeneratedKeyColumns("id");
    }

    public OrderItemEntity insert(final OrderItemEntity orderItemEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(orderItemEntity);
        final long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new OrderItemEntity(
                id,
                orderItemEntity.getName(),
                orderItemEntity.getImageUrl(),
                orderItemEntity.getPrice(),
                orderItemEntity.getQuantity(),
                orderItemEntity.getOrderId()
        );
    }

    public void insertAll(final List<OrderItemEntity> orderItemEntity) {
        final BeanPropertySqlParameterSource[] parameterSources = orderItemEntity.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(BeanPropertySqlParameterSource[]::new);
        jdbcInsert.executeBatch(parameterSources);
    }

    @Transactional(readOnly = true)
    public List<OrderItemEntity> findAllByOrderId(final Long orderId) {
        final String sql = "SELECT * FROM order_item WHERE order_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }

    @Transactional(readOnly = true)
    public List<OrderItemEntity> findAllByOrderIds(final List<Long> orderIds) {
        if (orderIds.isEmpty()) {
            return Collections.emptyList();
        }
        final String sql = "SELECT * FROM order_item WHERE order_id IN (:ids)";
        SqlParameterSource parameters = new MapSqlParameterSource("ids", orderIds);
        return namedJdbcTemplate.query(sql, parameters, rowMapper);
    }
}
