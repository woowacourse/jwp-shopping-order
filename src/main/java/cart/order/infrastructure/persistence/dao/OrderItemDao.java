package cart.order.infrastructure.persistence.dao;

import cart.common.annotation.Dao;
import cart.order.infrastructure.persistence.entity.OrderItemEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@Dao
public class OrderItemDao {

    private static final RowMapper<OrderItemEntity> orderItemRowMapper =
            new BeanPropertyRowMapper<>(OrderItemEntity.class);

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public void saveAll(List<OrderItemEntity> orderItems) {
        SqlParameterSource[] array = orderItems.stream()
                .map(BeanPropertySqlParameterSource::new)
                .collect(Collectors.toList())
                .toArray(new SqlParameterSource[orderItems.size()]);
        simpleJdbcInsert.executeBatch(array);
    }

    public List<OrderItemEntity> findAllByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_item WHERE order_id = ?";
        return jdbcTemplate.query(sql, orderItemRowMapper, orderId);
    }
}
