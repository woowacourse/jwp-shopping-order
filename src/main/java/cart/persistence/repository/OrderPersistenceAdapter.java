package cart.persistence.repository;

import cart.application.domain.Member;
import cart.application.domain.Order;
import cart.application.repository.OrderRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderPersistenceAdapter implements OrderRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final OrderInfosPersistenceAdapter orderInfosPersistenceAdapter;

    public OrderPersistenceAdapter(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.orderInfosPersistenceAdapter = new OrderInfosPersistenceAdapter(namedParameterJdbcTemplate);
    }

    @Override
    public Order insert(Order order) {
        Long orderId = saveIntoOrder(order);
        orderInfosPersistenceAdapter.insert(order.getOrderInfo(), orderId);
        return findById(orderId).orElseThrow();
    }

    private Long saveIntoOrder(Order order) {
        String sql = "INSERT INTO orders (member_id, original_price, used_point, point_to_add) " +
                "VALUES (:member_id, :original_price, :used_point, :point_to_add)";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("member_id", order.getMember().getId())
                .addValue("original_price", order.getOriginalPrice())
                .addValue("used_point", order.getUsedPoint())
                .addValue("point_to_add", order.getPointToAdd());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, namedParameters, keyHolder);
        return keyHolder.getKeyAs(Long.class);
    }

    @Override
    public List<Order> findByMemberId(Long id) {
        String sql = "SELECT * FROM orders " +
                "INNER JOIN member ON orders.member_id = member.id " +
                "WHERE member.id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);

        return namedParameterJdbcTemplate.query(sql, namedParameters, (rs, rowNum) ->
                new Order(
                        rs.getLong("orders.id"),
                        extractMember(rs),
                        orderInfosPersistenceAdapter.findByOrderId(rs.getLong("orders.id")),
                        rs.getInt("orders.original_price"),
                        rs.getInt("orders.used_point"),
                        rs.getInt("orders.point_to_add")
                )
        );
    }

    @Override
    public Optional<Order> findById(Long id) {
        String sql = "SELECT * FROM orders " +
                "INNER JOIN member ON orders.member_id = member.id " +
                "WHERE orders.id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);

        return namedParameterJdbcTemplate.query(sql, namedParameters, (rs, rowNum) ->
                new Order(
                        rs.getLong("orders.id"),
                        extractMember(rs),
                        orderInfosPersistenceAdapter.findByOrderId(rs.getLong("orders.id")),
                        rs.getInt("orders.original_price"),
                        rs.getInt("orders.used_point"),
                        rs.getInt("orders.point_to_add")
                )
        ).stream().findAny();
    }

    private Member extractMember(ResultSet rs) throws SQLException {
        return new Member(
                rs.getLong("member.id"),
                rs.getString("member.email"),
                rs.getString("member.password"),
                rs.getInt("member.point")
        );
    }
}
