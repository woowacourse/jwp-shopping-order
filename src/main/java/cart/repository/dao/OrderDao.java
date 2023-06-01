package cart.repository.dao;

import cart.repository.entity.OrderEntity;
import cart.repository.entity.OrderItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Objects;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long insertOrder(OrderEntity orderEntity) {
        final String query = "INSERT INTO orders (member_id, total_payment, used_point) VALUES (?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

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
        final String query = "INSERT INTO order_item (order_id, product_name, product_price, product_image_url, quantity) VALUES (?, ?, ?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

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
}
