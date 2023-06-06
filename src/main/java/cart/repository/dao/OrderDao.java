package cart.repository.dao;

import cart.repository.entity.OrderEntity;
import cart.repository.entity.OrderProductEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert ordersSimpleJdbcInsert;
    private final SimpleJdbcInsert orderItemSimpleJdbcInsert;

    private final RowMapper<OrderProductEntity> orderProductEntityRowMapper = (resultSet, rowNum) ->
            new OrderProductEntity.Builder()
                    .id(resultSet.getLong("id"))
                    .orderId(resultSet.getLong("order_id"))
                    .productId(resultSet.getLong("product_id"))
                    .productName(resultSet.getString("product_name"))
                    .productPrice(resultSet.getInt("product_price"))
                    .productImageUrl(resultSet.getString("product_image_url"))
                    .quantity(resultSet.getInt("quantity"))
                    .totalPrice(resultSet.getInt("total_price"))
                    .build();

    private final RowMapper<OrderEntity> orderEntityRowMapper = (resultSet, rowNum) ->
            new OrderEntity.Builder()
                    .id(resultSet.getLong("id"))
                    .memberId(resultSet.getLong("member_id"))
                    .totalPayment(resultSet.getInt("total_payment"))
                    .usedPoint(resultSet.getInt("used_point"))
                    .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                    .build();

    private final RowMapper<Long> orderIdRowMapper = (resultSet, rowNum) ->
            resultSet.getLong("orders.id");

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.ordersSimpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingColumns("member_id", "total_payment", "used_point")
                .usingGeneratedKeyColumns("id");
        this.orderItemSimpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }

    public long insertOrder(OrderEntity orderEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("member_id", orderEntity.getMemberId());
        params.put("total_payment", orderEntity.getTotalPayment());
        params.put("used_point", orderEntity.getUsedPoint());
        return (long) ordersSimpleJdbcInsert.executeAndReturnKey(params);
    }

    public long insertOrderItems(OrderProductEntity orderProductEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("order_id", orderProductEntity.getOrderId());
        params.put("product_id", orderProductEntity.getProductId());
        params.put("product_name", orderProductEntity.getProductName());
        params.put("product_price", orderProductEntity.getProductPrice());
        params.put("product_image_url", orderProductEntity.getProductImageUrl());
        params.put("quantity", orderProductEntity.getQuantity());
        params.put("total_price", orderProductEntity.getTotalPrice());
        return (long) orderItemSimpleJdbcInsert.executeAndReturnKey(params);
    }

    public Optional<List<Long>> getOrderIdsByMemberId(long memberId) {
        try {
            String query = "SELECT DISTINCT orders.id " +
                    "FROM order_item " +
                    "JOIN orders ON orders.id = order_item.order_id " +
                    "WHERE orders.member_id = ?";
            return Optional.of(jdbcTemplate.query(query, orderIdRowMapper, memberId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<OrderEntity> getOrderById(long orderId) {
        try {
            String query = "SELECT * FROM orders WHERE id = ?";
            return Optional.of(jdbcTemplate.queryForObject(query, orderEntityRowMapper, orderId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<List<OrderProductEntity>> getOrderProductsByOrderId(long orderId) {
        try {
            String query = "SELECT * FROM order_item WHERE order_id = ?";
            return Optional.of(jdbcTemplate.query(query, orderProductEntityRowMapper, orderId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
