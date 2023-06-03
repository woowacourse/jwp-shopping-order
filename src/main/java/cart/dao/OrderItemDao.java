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
                    rs.getString("product_image_url"),
                    rs.getInt("total_price")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(OrderItemEntity orderItemEntity) {
        final SqlParameterSource source = new BeanPropertySqlParameterSource(orderItemEntity);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
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

    public List<OrderItemEntity> findAllByMemberId(Long memberId) {
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.product_name, oi.product_price, oi.product_image_url, oi.total_price " +
                "FROM order_item oi " +
                "JOIN orders o ON oi.order_id = o.id " +
                "WHERE o.member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }
}
