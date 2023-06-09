package cart.dao;

import cart.entity.OrderItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<OrderItemEntity> rowMapper = (rs, rowNum) ->
            new OrderItemEntity(
                    rs.getLong("id"),
                    rs.getLong("order_id"),
                    rs.getLong("product_id"),
                    rs.getInt("quantity"),
                    rs.getInt("price")
            );


    public OrderItemDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public void save(List<OrderItemEntity> orderItemEntities) {
        final SqlParameterSource[] array = orderItemEntities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .collect(Collectors.toList())
                .toArray(new SqlParameterSource[orderItemEntities.size()]);

        insertAction.executeBatch(array);
    }

    public Map<Long, List<OrderItemEntity>> findOrderItemsByMemberId(Long memberId) {
        String sql = "select * from order_item " +
                "where order_id in ( select id from orders where member_id = ? )";

        List<OrderItemEntity> orderItemEntities = jdbcTemplate.query(sql, rowMapper, memberId);

        return orderItemEntities.stream()
                .collect(Collectors.groupingBy(
                        OrderItemEntity::getOrderId,
                        Collectors.toList()
                ));
    }

    public List<OrderItemEntity> findOrderItemsByOrderId(Long orderId) {
        String sql = "SELECT * from order_item WHERE order_id = ? ";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
