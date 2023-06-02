package cart.dao;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Point;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
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
    private static final RowMapper<Order> mapper = (rs, rowNum) -> {
        long orders_id = rs.getLong("orders_id");
        long member_id = rs.getLong("member_id");
        String email = rs.getString("email");
        String password = rs.getString("password");
        int point = rs.getInt("point");
        LocalDateTime orderDateTime = rs.getTimestamp("created_at").toLocalDateTime();
        Member member = new Member(member_id, email, password, new Point(point));
        return new Order(orders_id, member, orderDateTime);
    };
    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(long memberId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO orders (member_id) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, memberId);
            return ps;
        }, keyHolder);

        return (Long) Objects.requireNonNull(keyHolder.getKeys().get("id"));
    }

    public Optional<Order> findById(Long orderId) {
        String sql = "SELECT o.id as orders_id, m.id as member_id, m.email, m.password, m.point, o.created_at "
                + "FROM orders AS o "
                + "INNER JOIN member AS m "
                + "ON m.id = o.member_id "
                + "WHERE o.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, mapper, orderId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Order> findByMemberId(Long memberId) {
        String sql = "SELECT o.id as orders_id, m.id as member_id, m.email, m.password, m.point, o.created_at "
                + "FROM orders AS o "
                + "INNER JOIN member AS m "
                + "ON m.id = o.member_id "
                + "WHERE o.member_id = ?";
        return jdbcTemplate.query(sql, mapper, memberId);
    }
}
