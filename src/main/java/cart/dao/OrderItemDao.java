package cart.dao;

import cart.domain.OrderedItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long createOrderedItems(Long orderId, OrderedItem orderedItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO order_item (name, price, image_url, quantity, discount_rate, order_id) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, orderedItem.getName());
            ps.setInt(2, orderedItem.getOrice());
            ps.setString(3, orderedItem.getImageUrl());
            ps.setInt(4, orderedItem.getQuantity());
            ps.setInt(5, orderedItem.getDiscountRate());
            ps.setLong(6, orderId);

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();

    }

    public List<OrderedItem> findByOrderId(Long id) {
        String sql = "SELECT * FROM order_item where order_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long orderId = rs.getLong("order_id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            int quantity = rs.getInt("quantity");
            int discountRate = rs.getInt("discount_rate");

            return new OrderedItem(id, orderId, name, price, imageUrl, quantity, discountRate);
        }, id);
    }
}
