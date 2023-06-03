package cart.dao;

import cart.domain.OrderedItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
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
            String sql = "INSERT INTO order_item (name, price, image_url, quantity, discount_rate, order_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, orderedItem.getName());
            ps.setInt(2, orderedItem.getPrice());
            ps.setString(3, orderedItem.getImageUrl());
            ps.setInt(4, orderedItem.getQuantity());
            ps.setInt(5, orderedItem.getDiscountRate());
            ps.setLong(6, orderId);

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<OrderedItem> findByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_item where order_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            int quantity = rs.getInt("quantity");
            int discountRate = rs.getInt("discount_rate");

            return new OrderedItem(id, orderId, name, price, imageUrl, quantity, discountRate);
        }, orderId);
    }
}
