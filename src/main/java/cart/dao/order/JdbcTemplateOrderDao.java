package cart.dao.order;

import cart.domain.member.Member;
import cart.domain.order.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcTemplateOrderDao implements OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateOrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Order> findOrderById(Long id) {
        String sql = "SELECT o.earned_point, o.used_point, o.created_at, m.id, m.email " +
                "FROM orders as o " +
                "INNER JOIN member as m ON o.member_id = m.id " +
                "WHERE o.id = ?";
        List<Order> orders = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            Long memberId = rs.getLong("member.id");
            String email = rs.getString("member.email");
            Member member = new Member(memberId, email, null);
            Long earnedPoint = rs.getLong("orders.earned_point");
            Long usedPoint = rs.getLong("orders.used_point");
            Timestamp createdAt = rs.getTimestamp("orders.created_at");
            return new Order(id, member, usedPoint, earnedPoint, createdAt);
        });
        if (orders.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(orders.get(0));
    }

    @Override
    public List<Order> findAllOrdersByMemberId(Long memberId) {
        String sql = "SELECT o.id, o.earned_point, o.used_point, o.created_at, m.email " +
                "FROM orders as o " +
                "INNER JOIN member as m ON o.member_id = m.id " +
                "WHERE o.member_id = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            Long id = rs.getLong("orders.id");
            String email = rs.getString("member.email");
            Member member = new Member(memberId, email, null);
            Long earnedPoint = rs.getLong("orders.earned_point");
            Long usedPoint = rs.getLong("orders.used_point");
            Timestamp createdAt = rs.getTimestamp("orders.created_at");
            return new Order(id, member, usedPoint, earnedPoint, createdAt);
        });
    }

    @Override
    public Long createOrder(Order order, Long pointId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO orders (member_id, point_id, earned_point, used_point, created_at) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, order.getMember().getId());
            ps.setLong(2, pointId);
            ps.setLong(3, order.getEarnedPoint());
            ps.setLong(4, order.getUsedPoint());
            ps.setTimestamp(5, order.getCreatedAt());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
