package cart.dao;

import cart.dao.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDao {
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_record")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final OrderEntity orderEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("member_id", orderEntity.getMemberId());
        params.put("order_time", orderEntity.getOrderTime());
        return this.simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<OrderEntity> findByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM order_record WHERE member_id = ?";
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new OrderEntity(rs.getLong("id"), rs.getLong("member_id"), rs.getTimestamp("order_time"));
        }, memberId);
    }

    public OrderEntity findById(final Long orderId) {
        final String sql = "SELECT * FROM order_record WHERE id = ?";
        return this.jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            return new OrderEntity(rs.getLong("id"), rs.getLong("member_id"), rs.getTimestamp("order_time"));
        }, orderId);
    }
}
