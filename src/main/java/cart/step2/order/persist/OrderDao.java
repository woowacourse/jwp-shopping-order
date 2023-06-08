package cart.step2.order.persist;

import cart.step2.order.domain.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDao {

    private RowMapper<OrderEntity> rowMapper = (rs, rowNum) ->
        OrderEntity.of(
                rs.getLong("id"),
                rs.getInt("price"),
                rs.getLong("coupon_id"),
                rs.getLong("member_id"),
                rs.getTimestamp("date").toLocalDateTime()
        );

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(final OrderEntity orderEntity) {
        final String sql = "INSERT INTO orders (price, coupon_id, member_id) " +
                "VALUES (?, ?, ?)";
        return (long) jdbcTemplate.update(sql, orderEntity.getPrice(), orderEntity.getCouponId(), orderEntity.getMemberId());
    }

    public List<OrderEntity> findAllByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

}
