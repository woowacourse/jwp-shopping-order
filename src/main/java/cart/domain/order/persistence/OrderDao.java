package cart.domain.order.persistence;

import cart.domain.order.domain.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    public static final String JOIN_SQL = "SELECT cart_order.id, cart_order.member_id, member.email, member.password, member.cash, cart_order.total_price, cart_order.created_at " +
            "FROM cart_order " +
            "INNER JOIN member ON cart_order.member_id = member.id ";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_order").usingGeneratedKeyColumns("id");
    }

    public Order insert(Order order) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", order.getMember().getId())
                .addValue("total_price", order.getTotalPrice())
                .addValue("created_at", order.getCreatedAt());
        long insertedId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Order(insertedId, order.getMember(), order.getTotalPrice(), order.getCreatedAt());
    }

    public Boolean isNotExistById(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM cart_order WHERE id = ?)";
        return Boolean.FALSE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }
}
