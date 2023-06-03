package cart.persistence.order;

import cart.application.repository.order.OrderRepository;
import cart.domain.Member;
import cart.domain.order.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) ->
            new Member(rs.getLong("member.id"),
                    rs.getString("member.name"),
                    rs.getString("member.email"),
                    rs.getString("member.password")
            );
    private final RowMapper<Order> orderRowMapper = ((rs, rowNum) ->
            new Order(
                    rs.getLong("orders.id"),
                    rs.getInt("orders.payment_price"),
                    rs.getInt("orders.total_price"),
                    rs.getInt("orders.point"),
                    memberRowMapper.mapRow(rs, rowNum)
            ));


    @Override
    public Long createOrder(final Order order) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("member_id", order.getMember().getId());
        parameters.addValue("total_price", order.getTotalPrice());
        parameters.addValue("payment_price", order.getPaymentPrice());
        parameters.addValue("point", order.getPoint());
        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Order> findOrdersByMemberId(Long memberId) {
        String sql = "SELECT * FROM orders JOIN member ON member.id = orders.member_id WHERE orders.member_id = ?";
        return jdbcTemplate.query(sql, orderRowMapper, memberId);
    }

    @Override
    public Optional<Order> findOrderBy(long orderId) {
        String sql = "SELECT * FROM orders JOIN member ON member.id = orders.member_id WHERE orders.id = ?";
        try {
            Order order = jdbcTemplate.queryForObject(sql, orderRowMapper, orderId);
            return Optional.of(order);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
