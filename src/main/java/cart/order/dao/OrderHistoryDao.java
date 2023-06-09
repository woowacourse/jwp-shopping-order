package cart.order.dao;

import cart.member.domain.Member;
import cart.order.domain.OrderHistory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderHistoryDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public OrderHistoryDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("order_history")
                .usingGeneratedKeyColumns("id");
    }

    public List<OrderHistory> findByMemberId(final Long memberId) {
        final String sql = "SELECT order_history.id, order_history.total_price, order_history.created_at, member.email, member.password, member.cash " +
                "FROM order_history " +
                "INNER JOIN member ON order_history.member_id = member.id " +
                "WHERE member.id = ? " +
                "ORDER BY order_history.id DESC";
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            Long orderHistoryId = rs.getLong("order_history.id");
            Long totalPrice = rs.getLong("order_history.total_price");
            LocalDateTime created = rs.getTimestamp("order_history.created_at").toLocalDateTime();
            String email = rs.getString("member.email");
            String password = rs.getString("member.password");
            Long cash = rs.getLong("member.cash");
            Member member = Member.of(memberId, email, password, cash);
            return new OrderHistory(orderHistoryId, member, totalPrice, created);
        });
    }

    public OrderHistory findById(final Long orderHistoryId) {
        final String sql = "SELECT order_history.total_price, order_history.created_at, member.id, member.email, member.password, member.cash " +
                "FROM order_history " +
                "INNER JOIN member ON order_history.member_id = member.id " +
                "WHERE order_history.id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{orderHistoryId}, (rs, rowNum) -> {
            Long totalPrice = rs.getLong("order_history.total_price");
            LocalDateTime created = rs.getTimestamp("order_history.created_at").toLocalDateTime();
            Long memberId = rs.getLong("member.id");
            String email = rs.getString("member.email");
            String password = rs.getString("member.password");
            Long cash = rs.getLong("member.cash");
            Member member = Member.of(memberId, email, password, cash);
            return new OrderHistory(orderHistoryId, member, totalPrice, created);
        });
    }

    public Long save(final OrderHistory orderHistory) {
        final MapSqlParameterSource insertParameters = new MapSqlParameterSource()
                .addValue("member_id", orderHistory.getMember().getId())
                .addValue("total_price", orderHistory.getTotalPrice())
                .addValue("created_at", Timestamp.valueOf(LocalDateTime.now()));

        return insertAction.executeAndReturnKey(insertParameters).longValue();
    }
}
