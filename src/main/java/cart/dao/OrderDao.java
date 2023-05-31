package cart.dao;

import cart.domain.Order;
import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long createOrder(Long memberId, Order order) {
        String sql = "INSERT INTO orders (member_id, total_item_discount_amount, total_member_discount_amount, total_item_price, discounted_total_item_price, shipping_fee, total_price) VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, memberId);
            ps.setInt(2, order.getTotalItemDiscountAmount());
            ps.setInt(3, order.getTotalMemberDiscountAmount());
            ps.setInt(4, order.getTotalItemPrice());
            ps.setInt(5, order.getDiscountedTotalItemPrice());
            ps.setInt(6, order.getShippingFee());
            ps.setInt(7, order.getTotalPrice());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Order findByIds(Long memberId, Long orderId) {
        String sql = "SELECT * FROM orders WHERE id = ? AND member_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{orderId, memberId}, (rs, rowNum) -> {
            int totalItemDiscountAmount = rs.getInt("total_item_discount_amount");
            int totalMemberDiscountAmount = rs.getInt("total_member_discount_amount");
            int totalItemPrice = rs.getInt(("total_item_price"));
            int discountedTotalItemPrice = rs.getInt("discounted_total_item_price");
            int shippingFee = rs.getInt("shipping_fee");
            int totalPrice = rs.getInt("total_price");

            return new Order(orderId, memberId, null, totalItemDiscountAmount, totalMemberDiscountAmount, totalItemPrice, discountedTotalItemPrice, shippingFee, totalPrice);
        });
    }

    public Order findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Long memberId = rs.getLong("member_id");
            int totalItemDiscountAmount = rs.getInt("total_item_discount_amount");
            int totalMemberDiscountAmount = rs.getInt("total_member_discount_amount");
            int totalItemPrice = rs.getInt(("total_item_price"));
            int discountedTotalItemPrice = rs.getInt("discounted_total_item_price");
            int shippingFee = rs.getInt("shipping_fee");
            int totalPrice = rs.getInt("total_price");

            return new Order(id, memberId, null, totalItemDiscountAmount, totalMemberDiscountAmount, totalItemPrice, discountedTotalItemPrice, shippingFee, totalPrice);
        });
    }

    public List<Order> findByMemberId(Long memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            int totalItemDiscountAmount = rs.getInt("total_item_discount_amount");
            int totalMemberDiscountAmount = rs.getInt("total_member_discount_amount");
            int totalItemPrice = rs.getInt(("total_item_price"));
            int discountedTotalItemPrice = rs.getInt("discounted_total_item_price");
            int shippingFee = rs.getInt("shipping_fee");
            int totalPrice = rs.getInt("total_price");

            return new Order(id, memberId, null, totalItemDiscountAmount, totalMemberDiscountAmount, totalItemPrice, discountedTotalItemPrice, shippingFee, totalPrice);
        },memberId);
    }
}
