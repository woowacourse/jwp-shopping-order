package cart.dao;

import cart.entity.OrderEntity;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<OrderEntity> rowMapper = (rs, rowNum) ->
            new OrderEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getInt("used_point"),
                    rs.getInt("saved_point"),
                    rs.getTimestamp("ordered_at").toLocalDateTime()
            );

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(OrderEntity orderEntity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO shopping_order (member_id, used_point, saved_point, ordered_at) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, orderEntity.getMemberId());
            ps.setInt(2, orderEntity.getUsedPoint());
            ps.setInt(3, orderEntity.getSavedPoint());
            ps.setTimestamp(4, Timestamp.valueOf(orderEntity.getOrderedAt()));

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<OrderEntity> findByMemberId(Long memberId) {
        final String sql = "SELECT * from shopping_order WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public OrderEntity findById(Long id) {
        final String sql = "SELECT * FROM shopping_order WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
}
