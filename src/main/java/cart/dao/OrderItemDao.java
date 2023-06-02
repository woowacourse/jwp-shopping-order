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
                .usingColumns(
                        "order_id",
                        "product_id",
                        "quantity",
                        "product_name_at_order",
                        "product_price_at_order",
                        "product_image_url_at_order"
                );
    }

    public void saveAll(final List<OrderItemEntity> orderItemEntities) {
        final SqlParameterSource[] sqlParameterSources = orderItemEntities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        jdbcTemplate.executeBatch(sqlParameterSources);
    }

    public List<OrderItemEntity> findByOrderId(Long orderId) {
        final String sql = "SELECT * FROM ordered_item WHERE order_id = ?";
        try {
            return jdbcTemplate.getJdbcTemplate().query(sql, orderItemMapper(), orderId);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    private static RowMapper<OrderItemEntity> orderItemMapper() {
        return (rs, count) -> new OrderItemEntity(
                rs.getLong("id"),
                rs.getLong("order_id"), rs.getLong("product_id"),
                rs.getString("product_name_at_order"),
                rs.getInt("product_price_at_order"),
                rs.getString("product_image_url_at_order"),
                rs.getInt("quantity")
        );
    }
}
