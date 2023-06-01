package cart.dao;

import cart.domain.CartItem;
import cart.domain.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connect -> {
            PreparedStatement ps = connect.prepareStatement(
                    "INSERT INTO orders(member_id, product_price, discount_price, delivery_fee, total_price, created_at) " +
                            "VALUES(?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, order.getMember().getId());
            ps.setLong(2, order.getProductPrice());
            ps.setLong(3, order.getDiscountPrice());
            ps.setLong(4, order.getDeliveryFee());
            ps.setLong(5, order.getTotalPrice());
            ps.setDate(6, order.getDate());

            return ps;
        }, keyHolder);

        long orderId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        String relatedSql = "INSERT INTO order_items (order_id, product_name, product_price, product_image_url, product_quantity) " +
                "VALUES(?, ?, ?, ?, ?)";

        for (OrderItem orderItem : order.getOrderItems().getOrderItems()) {
            jdbcTemplate.update(relatedSql, orderId, orderItem.getProduct().getName(), orderItem.getProduct().getPrice(), orderItem.getProduct().getImageUrl(), orderItem.getQuantity());
        }

        return orderId;
    }


}
