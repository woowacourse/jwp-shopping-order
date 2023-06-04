package cart.dao;

import cart.dao.entity.OrderItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<OrderItemEntity> rowMapper = (resultSet, rowNum) -> new OrderItemEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getLong("price"),
            resultSet.getString("image_url"),
            resultSet.getLong("quantity"),
            resultSet.getLong("product_id"),
            resultSet.getLong("order_id")
    );

    public OrderItemDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public void createOrderItem(OrderItemEntity orderItemEntity) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(orderItemEntity);

        simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<OrderItemEntity> findAllByOrderId(final Long orderId) {
        final String sql = "SELECT * FROM order_item WHERE order_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }

    public List<OrderItemEntity> findAllByOrderIds(final List<Long> orderIds) {
        if (orderIds.isEmpty()) {
            return Collections.emptyList();

        }
        final String sql = "SELECT * FROM order_item WHERE order_id IN (:ids)";
        SqlParameterSource parameters = new MapSqlParameterSource("ids", orderIds);
        return namedJdbcTemplate.query(sql, parameters, rowMapper);
    }
}
