package cart.repository.dao;

import cart.entity.OrderEntity;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertOrders;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertOrders = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingColumns("member_id", "original_price", "discounted_price")
                .usingGeneratedKeyColumns("id");
    }

    public Long createOrder(final OrderEntity order) {
        Map<String, Object> params = new HashMap<>();
        params.put("member_id", order.getMemberId());
        params.put("original_price", order.getOriginalPrice());
        params.put("discounted_price", order.getDiscountedPrice());

        return insertOrders.executeAndReturnKey(params).longValue();
    }

    public Optional<OrderEntity> findById(final Long orderId) {
        String sql = "SELECT id, member_id, original_price, discounted_price, created_at FROM orders WHERE id = ?";
        try {
            OrderEntity order = jdbcTemplate.queryForObject(sql, orderRowMapper, orderId);
            return Optional.ofNullable(order);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<OrderEntity> findAllByMemberId(final Long memberId) {
        String sql = "SELECT id, member_id, original_price, discounted_price, created_at FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, orderRowMapper, memberId);
    }

    private final RowMapper<OrderEntity> orderRowMapper = (rs, rowNum) -> {
        final long id = rs.getLong("id");
        final long memberId = rs.getLong("member_id");
        final int originalPrice = rs.getInt("original_price");
        final int discountedPrice = rs.getInt("discounted_price");
        final LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        return new OrderEntity(id, memberId, originalPrice, discountedPrice, createdAt);
    };
}
