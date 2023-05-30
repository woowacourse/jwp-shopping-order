package cart.dao.order;

import cart.entity.order.OrderTableEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_table")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<OrderTableEntity> rowMapper = (rs, rowNum) ->
            new OrderTableEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getString("created_at"),
                    rs.getInt("delivery_fee")
            );

    public List<OrderTableEntity> findAllOrderEntitiesByMemberId(final long memberId) {
        String sql = "SELECT * FROM order_table WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public OrderTableEntity findById(final Long orderId) {
        String sql = "SELECT * FROM order_table WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, orderId);
    }

    public Long save(final long memberId, final int deliveryFee) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("member_id", memberId);
        parameters.put("created_at", Timestamp.valueOf(LocalDateTime.now()));
        parameters.put("delivery_fee", deliveryFee);

        Number generatedId = simpleJdbcInsert.executeAndReturnKey(parameters);

        return generatedId.longValue();
    }
}
