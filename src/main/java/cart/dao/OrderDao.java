package cart.dao;

import cart.domain.Order;
import cart.domain.Product;
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

    public Long createOrder(Long memberId, Order order) {
        String sql = "INSERT INTO orders (member_id, total_purchase_amount, total_item_price, shipping_fee, discounted_total_price) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, memberId);
            ps.setInt(2, order.getTotalPurchaseAmount());
            ps.setInt(3, order.getTotalItemPrice());
            ps.setInt(4, order.getShippingFee());
            ps.setInt(5, order.getDiscountedTotalPrice());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Order findById(Long orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{orderId}, (rs, rowNum) -> {
            Long memberId = rs.getLong("member_id");
            int totalPurchaseAmount = rs.getInt("total_purchase_amount");
            int totalItemPrice = rs.getInt(("total_item_price");
            int shippingFee = rs.getInt(("shipping_fee");
            int discountedTotalPrice = rs.getInt("discounted_total_price");

            return new Order(orderId, memberId, totalPurchaseAmount, totalItemPrice,null, shippingFee, discountedTotalPrice);
        });
    }
}
