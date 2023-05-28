package cart.dao;

import cart.domain.Member;
import cart.domain.Order;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public List<Order> findAll() {
        final String findAllQuery =
                "SELECT orders.id, orders.member_id, orders.created_at, orders.total_price, orders.final_price, member.id, member.email, member.password "
                        + "FROM orders "
                        + "INNER JOIN member ON orders.member_id = member.id";
        return jdbcTemplate.query(findAllQuery, (rs, rowNum) -> {
            final long orderId = rs.getLong("id");
            final LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
            final int totalPrice = rs.getInt("total_price");
            final int finalPrice = rs.getInt("final_price");
            final Long memberId = rs.getLong("member.id");
            final String email = rs.getString("member.email");
            final String password = rs.getString("member.password");
            final Member member = new Member(memberId, email, password);
            return new Order(orderId, createdAt, member, totalPrice, finalPrice);
        });
    }

    public Long saveOrder(final Order order) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("member_id", order.getMember().getId())
                .addValue("total_price", order.getTotalPrice())
                .addValue("final_price", order.getFinalPrice())
                .addValue("created_at", LocalDateTime.now());

        return insertAction.executeAndReturnKey(parameters).longValue();
    }
}
