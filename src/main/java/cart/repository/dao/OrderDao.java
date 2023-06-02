package cart.repository.dao;

import cart.repository.entity.OrderEntity;
import cart.repository.entity.OrderItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<OrderItemEntity> orderItemEntityRowMapper = (resultSet, rowNum) ->
            new OrderItemEntity.Builder()
                    .id(resultSet.getLong("id"))
                    .orderId(resultSet.getLong("order_id"))
                    .productName(resultSet.getString("product_name"))
                    .productPrice(resultSet.getInt("product_price"))
                    .productImageUrl(resultSet.getString("product_image_url"))
                    .quantity(resultSet.getInt("quantity"))
                    .build();

    private final RowMapper<Long> orderIdRowMapper = (resultSet, rowNum) ->
            resultSet.getLong("orders.id");

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long insertOrder(OrderEntity orderEntity) {
        String query = "INSERT INTO orders (member_id, total_payment, used_point) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            final PreparedStatement preparedStatement = con.prepareStatement(
                    query, new String[]{"ID"}
            );
            preparedStatement.setLong(1, orderEntity.getMemberId());
            preparedStatement.setInt(2, orderEntity.getTotalPayment());
            preparedStatement.setInt(3, orderEntity.getUsedPoint());
            return preparedStatement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey().longValue());
    }

    public long insertOrderItems(OrderItemEntity orderItemEntity) {
        String query = "INSERT INTO order_item (order_id, product_name, product_price, product_image_url, quantity) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            final PreparedStatement preparedStatement = con.prepareStatement(
                    query, new String[]{"ID"}
            );
            preparedStatement.setLong(1, orderItemEntity.getOrderId());
            preparedStatement.setString(2, orderItemEntity.getProductName());
            preparedStatement.setInt(3, orderItemEntity.getProductPrice());
            preparedStatement.setString(4, orderItemEntity.getProductImageUrl());
            preparedStatement.setInt(5, orderItemEntity.getQuantity());
            return preparedStatement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey().longValue());
    }

    public List<OrderItemEntity> getOrdersByMemberId(long memberId) {
        String query = "SELECT order_item.id, order_item.order_id, order_item.product_name, order_item.product_price, order_item.product_image_url, order_item.quantity " +
                "FROM order_item " +
                "JOIN orders ON orders.id = order_item.order_id " +
                "WHERE orders.member_id = ?";
        return jdbcTemplate.query(query, orderItemEntityRowMapper, memberId);
    }

    public List<Long> getOrderIdsByMemberId(long memberId) {
        String query = "SELECT orders.id " +
                "FROM order_item " +
                "JOIN orders ON orders.id = order_item.order_id " +
                "WHERE orders.member_id = ?";
        return jdbcTemplate.query(query, orderIdRowMapper, memberId);
    }

    private final RowMapper<OrderEntity> orderEntityRowMapper = (resultSet, rowNum) ->
            new OrderEntity.Builder()
                    .id(resultSet.getLong("id"))
                    .memberId(resultSet.getLong("member_id"))
                    .totalPayment(resultSet.getInt("total_payment"))
                    .usedPoint(resultSet.getInt("used_point"))
                    .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                    .build();

    public OrderEntity getOrderById(long orderId) {
        String query = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(query, orderEntityRowMapper, orderId);
    }

    public List<OrderItemEntity> getOrderItemByOrderId(long orderId) {
        String query = "SELECT * FROM order_item WHERE order_id = ?";
        return jdbcTemplate.query(query, orderItemEntityRowMapper, orderId);
    }
}
