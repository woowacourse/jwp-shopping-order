package cart.dao;

import cart.domain.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.entity.OrderEntity;
import cart.entity.OrderProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<OrderEntity> orderEntityRowMapper = (rs, rowNum) -> new OrderEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getInt("price")
    );
    private final RowMapper<OrderProductEntity> orderProductEntityRowMapper = (rs, rowNum) -> new OrderProductEntity(
            rs.getLong("id"),
            rs.getLong("order_id"),
            rs.getLong("product_id"),
            rs.getInt("quantity")
    );

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long save(final Member member, final Order order) {
        final long orderId = insertOrder(member, order);
        insertOrderItem(orderId, order);
        return orderId;
    }

    private long insertOrder(final Member member, final Order order) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(

                    "INSERT INTO user_order (member_id, price) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, member.getId());
            ps.setInt(2, order.price());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    private void insertOrderItem(final long orderId, final Order order) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO user_order_item (order_id, product_id, quantity) VALUES (?, ?, ?)",
                order.getProducts(),
                100,
                (final PreparedStatement ps, final OrderProduct orderProduct) -> {
                    ps.setLong(1, orderId);
                    ps.setLong(2, orderProduct.getProduct().getId());
                    ps.setLong(3, orderProduct.getQuantity());
                }
        );
    }

    public OrderEntity findById(final long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM user_order WHERE id = ? ",
                orderEntityRowMapper,
                id
        );
    }

    public List<OrderProductEntity> findByOrderId(final long orderId) {
        return jdbcTemplate.query(
                "SELECT * FROM user_order_item WHERE order_id = ? ",
                orderProductEntityRowMapper,
                orderId
        );
    }
}
