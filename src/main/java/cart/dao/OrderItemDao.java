package cart.dao;

import cart.entity.OrderItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
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


    public OrderItemDao(JdbcTemplate jdbcTemplate) {
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

        return jdbcTemplate.query(sql, rs -> {
            Map<Long, List<OrderItemEntity>> resultMap = new HashMap<>();
            while (rs.next()) {
                Long id = rs.getLong("id");
                Long orderId = rs.getLong("order_id");
                Long productId = rs.getLong("product_id");
                int quantity = rs.getInt("quantity");
                int price = rs.getInt("price");

                OrderItemEntity orderItem = new OrderItemEntity(id, orderId, productId, quantity, price);
                resultMap.computeIfAbsent(orderId, k -> new ArrayList<>()).add(orderItem);
            }
            return resultMap;
        }, memberId);
    }

    public List<OrderItemEntity> findOrderItemsByOrderId(Long orderId) {
        String sql = "SELECT * from order_item WHERE order_id = ? ";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
