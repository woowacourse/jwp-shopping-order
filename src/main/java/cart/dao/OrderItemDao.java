package cart.dao;

import cart.entity.OrderItemEntity;
import java.util.Collections;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private final SimpleJdbcInsert jdbcTemplate;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("ordered_item")
                .usingColumns("product_id", "order_id", "quantity");
    }

    public List<OrderItemEntity> findByOrderId(Long id) {
        final String sql = "SELECT * FROM ordered_item WHERE order_id = ?";
        try {
            return jdbcTemplate.getJdbcTemplate().query(sql, orderItemMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    private static RowMapper<OrderItemEntity> orderItemMapper() {
        return (rs, count) -> new OrderItemEntity(
                rs.getLong("id"),
                rs.getLong("product_id"),
                rs.getLong("order_id"),
                rs.getInt("quantity")
        );
    }

    public void create(final List<OrderItemEntity> orderItemEntities) {
        final SqlParameterSource[] sqlParameterSources = orderItemEntities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        jdbcTemplate.executeBatch(sqlParameterSources);
    }
}
