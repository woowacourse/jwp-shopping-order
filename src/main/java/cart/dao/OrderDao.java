package cart.dao;

import cart.entity.OrderEntity;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
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

    private static final RowMapper<OrderEntity> rowMapper = ((rs, rowNum) -> {
        Long id = rs.getLong("id");
        Long memberId = rs.getLong("member_id");
        String orderNumber = rs.getString("orders_number");
        int deliveryFee = rs.getInt("delivery_fee");
        Timestamp createdAt = rs.getTimestamp("created_at");
        return new OrderEntity(id, memberId, orderNumber, deliveryFee, createdAt.toLocalDateTime());
    });

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(OrderEntity order) {
        String sql = "INSERT INTO orders (member_id, orders_number, delivery_fee, created_at) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, order.getMemberId());
            ps.setString(2, order.getOrderNumber());
            ps.setInt(3, order.getDeliveryFee());
            ps.setTimestamp(4, Timestamp.valueOf(order.getCreatedAt()));

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<OrderEntity> findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";

        try {
            OrderEntity orderEntity = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(orderEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<OrderEntity> findAllByMemberId(Long memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ?";

        return jdbcTemplate.query(sql, rowMapper, memberId);
    }
}
