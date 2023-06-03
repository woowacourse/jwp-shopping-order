package cart.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import cart.repository.OrderDto;
import cart.repository.OrderEntity;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleInsert;
    private final RowMapper<OrderDto> rowMapper = (rs, rowNum) ->
        new OrderDto(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getTimestamp("order_at").toLocalDateTime(),
            rs.getInt("pay_amount"),
            rs.getString("order_status"),
            rs.getInt("quantity"),
            rs.getLong("product_id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("image_url")
        );

    public OrderDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("orders")
            .usingGeneratedKeyColumns("id");
    }

    public List<OrderDto> findAllByMemberId(Long memberId) {
        String sql = "SELECT o.id AS id, m.id AS member_id, m.email AS email, m.password AS password, "
            + "o.order_at AS order_at, o.pay_amount as pay_amount, o.order_status AS order_status, "
            + "op.quantity AS quantity, p.id AS product_id, p.name AS name, p.price AS price, p.image_url AS image_url FROM orders AS o "
            + "LEFT JOIN member AS m ON o.member_id = m.id "
            + "RIGHT OUTER JOIN order_product AS op ON o.id = op.order_id "
            + "INNER JOIN product AS p ON op.product_id = p.id "
            + "WHERE m.id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public List<OrderDto> findById(Long id) {
        String sql = "SELECT o.id AS id, m.id AS member_id, m.email AS email, m.password AS password, "
            + "o.order_at AS order_at, o.pay_amount as pay_amount, o.order_status AS order_status, "
            + "op.quantity AS quantity, p.id AS product_id, p.name AS name, p.price AS price, p.image_url AS image_url FROM orders AS o "
            + "LEFT JOIN member AS m ON o.member_id = m.id "
            + "RIGHT OUTER JOIN order_product AS op ON o.id = op.order_id "
            + "INNER JOIN product AS p ON op.product_id = p.id "
            + "WHERE o.id = ?";
        return jdbcTemplate.query(sql, rowMapper, id);
    }

    public Long insert(OrderEntity order) {
        Map<String, Object> params = Map.of(
            "member_id", order.getMemberId(),
            "order_at", order.getOrderAt(),
            "pay_amount", order.getPayAmount(),
            "order_status", order.getOrderStatus()
        );
        return simpleInsert.executeAndReturnKey(params).longValue();
    }

    public void update(OrderEntity order) {
        String sql = "UPDATE orders SET member_id = ?, order_at = ?, pay_amount = ?, order_status = ? WHERE id = ?";
        jdbcTemplate.update(sql, order.getMemberId(), order.getOrderAt(), order.getPayAmount(), order.getOrderStatus(),
            order.getId());
    }
}
