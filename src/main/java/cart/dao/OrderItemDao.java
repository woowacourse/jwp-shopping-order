package cart.dao;

import cart.entity.OrderItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
    }

    public void save(List<OrderItemEntity> orderItemEntities) {
        final SqlParameterSource[] array = orderItemEntities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .collect(Collectors.toList())
                .toArray(new SqlParameterSource[orderItemEntities.size()]);

        insertAction.executeBatch(array);

//        String sql = "INSERT INTO order_item (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
//
//        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                ps.setLong(1, orderId);
//                ps.setLong(2, orderItems.get(i).getProduct().getId());
//                ps.setLong(3, orderItems.get(i).getQuantity());
//                ps.setLong(4, orderItems.get(i).getProduct().getPrice().price());
//            }
//
//            @Override
//            public int getBatchSize() {
//                return orderItems.size();
//            }
//        });
//        orderItems.clear();
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

    /*public Map<Long, List<OrderItemEntity>> findByOrderIds(List<Long> orderIds) {
        String sql = "SELECT * from order_item WHERE order_id IN (:orderIds)";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("orderIds", orderIds);

        return namedParameterJdbcTemplate.query(sql, parameters, rs -> {
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
        });
    }*/
}
