package cart.dao;

import cart.domain.Member;
import cart.domain.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertOrders;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.insertOrders = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
    }

    public Order findById(Long id) {
        String sql = "SELECT orders.id, orders.member_id, orders.original_price, orders.used_point, orders.point_to_add, member.email, member.point " +
                "FROM orders " +
                "INNER JOIN member ON orders.member_id = member.id " +
                "WHERE orders.id = ?";

        return jdbcTemplate.queryForObject(sql, new OrderRowMapper(), id);
    }

    public List<Order> findByMemberId(Long memberId) {
        String sql = "SELECT orders.id, orders.member_id, orders.original_price, orders.used_point, orders.point_to_add, member.email, member.point " +
                "FROM orders " +
                "INNER JOIN member ON orders.member_id = member.id " +
                "WHERE orders.member_id = ?";

        return jdbcTemplate.query(sql, new OrderRowMapper(), memberId);
    }

    public Long save(Order order) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("member_id", order.getMember().getId());
        parameters.put("original_price", order.getOriginalPrice());
        parameters.put("used_point", order.getUsedPoint());
        parameters.put("point_to_add", order.getPointToAdd());

        return insertOrders.executeAndReturnKey(parameters).longValue();
    }

    private static class OrderRowMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long memberId = rs.getLong("orders.member_id");
            String email = rs.getString("member.email");
            Long point = rs.getLong("member.point");
            return new Order(
                    rs.getLong("orders.id"),
                    new Member(memberId, email, point),
                    rs.getLong("orders.original_price"),
                    rs.getLong("orders.used_point"),
                    rs.getLong("orders.point_to_add")
            );
        }
    }
}
