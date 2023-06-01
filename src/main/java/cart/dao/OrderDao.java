package cart.dao;

import cart.entity.OrderEntity;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<OrderEntity> rowMapper = (rs, rowNum) -> new OrderEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getInt("earned_points"),
            rs.getInt("used_points"),
            rs.getInt("total_price"),
            rs.getInt("pay_price"),
            rs.getTimestamp("order_date").toLocalDateTime()
    );

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(OrderEntity orderEntity) {
        String sql = "INSERT INTO tb_order(member_id, earned_points, used_points, total_price, pay_price, order_date) "
                + "VALUES(?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setLong(1, orderEntity.getMemberId());
            ps.setLong(2, orderEntity.getEarnedPoints());
            ps.setInt(3, orderEntity.getUsedPoints());
            ps.setInt(4, orderEntity.getTotalPrice());
            ps.setInt(5, orderEntity.getPayPrice());
            ps.setTimestamp(6, Timestamp.valueOf(orderEntity.getOrderDate()));

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<OrderEntity> findByOrderId(Long orderId) {
        String sql = "SELECT id, member_id, earned_points, used_points, total_price, pay_price, order_date "
                + "FROM tb_order "
                + "WHERE id = ?";

        return jdbcTemplate.query(sql, rowMapper, orderId)
                .stream()
                .findAny();
    }

    public List<OrderEntity> findByMemberIdAndLastOrderIdAndSize(Long memberId, Long lastOrderId, int size) {
        String sql = "SELECT id, member_id, earned_points, used_points, total_price, pay_price, order_date "
                + "FROM tb_order "
                + "WHERE member_id = ? AND id < ? "
                + "ORDER BY order_date DESC "
                + "LIMIT ?";

        return jdbcTemplate.query(sql, rowMapper, memberId, lastOrderId, size);
    }
}
