package cart.repository.dao;

import cart.repository.entity.OrderEntity;
import cart.repository.entity.OrderProductEntity;
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

    public long insertOrderItems(OrderProductEntity orderProductEntity) {
        String query = "INSERT INTO order_item (order_id, product_id, product_name, product_price, product_image_url, quantity, total_price) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            final PreparedStatement preparedStatement = con.prepareStatement(
                    query, new String[]{"ID"}
            );
            preparedStatement.setLong(1, orderProductEntity.getOrderId());
            preparedStatement.setLong(2, orderProductEntity.getProductId());
            preparedStatement.setString(3, orderProductEntity.getProductName());
            preparedStatement.setInt(4, orderProductEntity.getProductPrice());
            preparedStatement.setString(5, orderProductEntity.getProductImageUrl());
            preparedStatement.setInt(6, orderProductEntity.getQuantity());
            preparedStatement.setInt(7, orderProductEntity.getTotalPrice());
            return preparedStatement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey().longValue());
    }

    public List<Long> getOrderIdsByMemberId(long memberId) {
        String query = "SELECT DISTINCT orders.id " +
                "FROM order_item " +
                "JOIN orders ON orders.id = order_item.order_id " +
                "WHERE orders.member_id = ?";
        return jdbcTemplate.query(query, orderIdRowMapper, memberId);
    }

    public OrderEntity getOrderById(long orderId) {
        String query = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(query, orderEntityRowMapper, orderId);
    }

    public List<OrderProductEntity> getOrderItemsByOrderId(long orderId) {
        String query = "SELECT * FROM order_item WHERE order_id = ?";
        return jdbcTemplate.query(query, orderProductEntityRowMapper, orderId);
    }
}
