package cart.order.dao;

import cart.member.domain.Member;
import cart.order.domain.CartOrder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CartOrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public CartOrderDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("cart_order")
                .usingGeneratedKeyColumns("id");
    }

    public List<CartOrder> findByMemberId(final Long memberId) {
        final String sql = "SELECT cart_order.id, cart_order.total_price, cart_order.created_at, member.email, member.cash " +
                "FROM cart_order " +
                "INNER JOIN member ON cart_order.member_id = member.id " +
                "WHERE member.id = ? " +
                "ORDER BY cart_order.id DESC";
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            Long cartOrderId = rs.getLong("cart_order.id");
            Long totalPrice = rs.getLong("cart_order.total_price");
            LocalDateTime created = rs.getTimestamp("cart_order.created_at").toLocalDateTime();
            String email = rs.getString("member.email");
            Long cash = rs.getLong("member.cash");
            Member member = Member.of(memberId, email, null, cash);
            return new CartOrder(cartOrderId, member, totalPrice, created);
        });
    }

    public CartOrder findById(final Long cartOrderId) {
        final String sql = "SELECT cart_order.total_price, cart_order.created_at, member.id, member.email, member.cash " +
                "FROM cart_order " +
                "INNER JOIN member ON cart_order.member_id = member.id " +
                "WHERE cart_order.id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{cartOrderId}, (rs, rowNum) -> {
            Long totalPrice = rs.getLong("cart_order.total_price");
            LocalDateTime created = rs.getTimestamp("cart_order.created_at").toLocalDateTime();
            Long memberId = rs.getLong("member.id");
            String email = rs.getString("member.email");
            Long cash = rs.getLong("member.cash");
            Member member = Member.of(memberId, email, null, cash);
            return new CartOrder(cartOrderId, member, totalPrice, created);
        });
    }

    public Long save(final CartOrder cartOrder) {
        final MapSqlParameterSource insertParameters = new MapSqlParameterSource()
                .addValue("member_id", cartOrder.getMember().getId())
                .addValue("total_price", cartOrder.getTotalPrice())
                .addValue("created_at", Timestamp.valueOf(LocalDateTime.now()));
        ;

        return insertAction.executeAndReturnKey(insertParameters).longValue();
    }
}
