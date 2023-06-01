package cart.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cart.domain.Order;

@Repository
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long addOrder(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO `order` (member_id, member_coupon_id, shipping_fee, total_price) VALUES (?, ?, ?, ?)",
                new String[] { "ID" }); // "ID" 컬럼을 명시적으로 지정

            ps.setLong(1, order.getMember().getId());
            ps.setLong(2, order.getCoupon().getId());
            ps.setInt(3, order.getShippingFee().value());
            ps.setInt(4, order.getDiscountedPrice().value());

            return ps;
        }, keyHolder);

        List<Map<String, Object>> keys = keyHolder.getKeyList();
        if (!keys.isEmpty()) {
            return ((Number) keys.get(0).get("ID")).longValue(); // 첫 번째 키 값 반환
        } else {
            throw new IllegalStateException("Failed to retrieve generated key");
        }
    }
}
