package cart.dao;

import static java.util.stream.Collectors.toList;

import cart.entity.OrderItemEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private final static RowMapper<OrderItemEntity> rowMapper = (rs, rowNum) ->
            new OrderItemEntity(
                    rs.getLong("id"),
                    rs.getLong("order_id"),
                    rs.getLong("product_id"),
                    rs.getInt("quantity"),
                    rs.getString("product_name"),
                    rs.getInt("product_price"),
                    rs.getString("product_image_url")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public void batchSave(List<OrderItemEntity> orderItemEntities) {
        final SqlParameterSource[] array = orderItemEntities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .collect(toList())
                .toArray(new SqlParameterSource[orderItemEntities.size()]);
        simpleJdbcInsert.executeBatch(array);
    }

    public List<OrderItemEntity> findAllByOrderId(Long orderId) {
        String sql = "select * from order_item where order_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
