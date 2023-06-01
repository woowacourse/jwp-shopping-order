package cart.dao;

import cart.domain.CartOrder;
import cart.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public class CartOrderDao {

    private final JdbcTemplate jdbcTemplate;

    public CartOrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO cart_order (member_id, total_price) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, cartOrder.getMember().getId());
            ps.setLong(2, cartOrder.getTotalPrice());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
