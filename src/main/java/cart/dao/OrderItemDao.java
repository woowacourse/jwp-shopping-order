package cart.dao;

import cart.dao.entity.OrderItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderItemDao {
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final OrderItemEntity orderItemEntity) {
        final BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(orderItemEntity);
        return this.simpleJdbcInsert.executeAndReturnKey(beanPropertySqlParameterSource).longValue();
    }

    public List<OrderItemEntity> findByOrderId(final long orderId) {
        final String sql = "SELECT * FROM order_item WHERE order_id = ?";
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new OrderItemEntity(rs.getLong("id"), rs.getString("name"), rs.getInt("quantity"), rs.getString("image_url"), rs.getInt("total_price"), rs.getLong("order_id"));
        }, orderId);
    }
}
