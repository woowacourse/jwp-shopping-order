package cart.dao;

import cart.entity.OrderEntity;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private static final RowMapper<OrderEntity> ROW_MAPPER = ((rs, rowNum) -> {
        Long id = rs.getLong("id");
        Long memberId = rs.getLong("member_id");
        Long couponId = rs.getLong("coupon_id");
        String orderNumber = rs.getString("orders_number");
        int deliveryFee = rs.getInt("delivery_fee");
        Timestamp createdAt = rs.getTimestamp("created_at");
        return new OrderEntity(id, memberId, couponId, orderNumber, deliveryFee, createdAt.toLocalDateTime());
    });

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(OrderEntity order) {
        String sql = "INSERT INTO orders (member_id, coupon_id, orders_number, delivery_fee, created_at) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, order.getMemberId());
            setCouponId(ps, order.getCouponId());
            ps.setString(3, order.getOrderNumber());
            ps.setInt(4, order.getDeliveryFee());
            ps.setTimestamp(5, Timestamp.valueOf(order.getCreatedAt()));

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    private void setCouponId(PreparedStatement ps, Long couponId) throws SQLException {
        if (Objects.isNull(couponId)) {
            ps.setNull(2, Types.BIGINT);
            return;
        }
        ps.setLong(2, couponId);

    }

    public Optional<OrderEntity> findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";

        try {
            OrderEntity orderEntity = jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
            return Optional.ofNullable(orderEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<OrderEntity> findAllByMemberId(Long memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ?";

        return jdbcTemplate.query(sql, ROW_MAPPER, memberId);
    }
}
