package cart.persistence.dao;

import cart.persistence.entity.OrderEntity;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(final OrderEntity orderEntity) {
        final String query = "INSERT INTO `order`(member_id, total_price, discounted_total_price, "
            + "delivery_price, ordered_at) VALUES (?, ?, ?, ?, ?)";

        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
            ps.setLong(1, orderEntity.getMemberId());
            ps.setInt(2, orderEntity.getTotalPrice());
            ps.setInt(3, orderEntity.getDiscountedTotalPrice());
            ps.setInt(4, orderEntity.getDeliveryPrice());
            ps.setTimestamp(5, Timestamp.valueOf(orderEntity.getOrderedAt()));
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Long countByMemberId(final Long memberId) {
        final String sql = "SELECT COUNT(*) FROM `order` WHERE member_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, memberId);
    }
}
