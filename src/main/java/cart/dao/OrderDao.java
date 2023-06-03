package cart.dao;

import cart.domain.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long createOrder(Long memberId, Order order) {
        String sql = "INSERT INTO orders (total_item_price, discounted_total_item_price, shipping_fee, ordered_at, member_id) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, order.getTotalItemPrice());
            ps.setInt(2, order.getDiscountedTotalItemPrice());
            ps.setInt(3, order.getShippingFee());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(5, memberId);
            return ps;
        }, keyHolder);


        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Order findByIds(Long memberId, Long orderId) {
        String sql = "SELECT * FROM orders WHERE id = ? AND member_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{orderId, memberId}, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            int totalItemPrice = rs.getInt("total_item_price");
            int discountedTotalItemPrice = rs.getInt("discounted_total_item_price");
            int shippingFee = rs.getInt("shipping_fee");
            LocalDateTime orderedAt = rs.getTimestamp("ordered_at").toLocalDateTime();

            return new Order(id, totalItemPrice, discountedTotalItemPrice, shippingFee, orderedAt, memberId);
        });
    }

    public Order findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Long memberId = rs.getLong("member_id");
            LocalDateTime orderedAt = rs.getTimestamp("ordered_at").toLocalDateTime();
            int totalItemPrice = rs.getInt(("total_item_price"));
            int discountedTotalItemPrice = rs.getInt("discounted_total_item_price");
            int shippingFee = rs.getInt("shipping_fee");

            return new Order(id, totalItemPrice, discountedTotalItemPrice, shippingFee, orderedAt, memberId);
        });
    }

    public List<Order> findAllByMemberId(Long memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            LocalDateTime orderedAt = rs.getTimestamp("ordered_at").toLocalDateTime();
            int totalItemPrice = rs.getInt(("total_item_price"));
            int discountedTotalItemPrice = rs.getInt("discounted_total_item_price");
            int shippingFee = rs.getInt("shipping_fee");

            return new Order(id, totalItemPrice, discountedTotalItemPrice, shippingFee, orderedAt, memberId);
        },memberId);
    }
}
