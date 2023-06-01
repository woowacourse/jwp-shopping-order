package cart.dao;

import cart.repository.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    
    private final RowMapper<OrderEntity> rowMapper = (rs, rowNum) ->
            new OrderEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getLong("original_price"),
                    rs.getLong("used_point"),
                    rs.getLong("point_to_add")
            );
    
    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }
    
    public Long insert(final OrderEntity orderEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("member_id", orderEntity.getMemberId());
        params.put("original_price", orderEntity.getOriginalPrice());
        params.put("used_point", orderEntity.getUsedPoint());
        params.put("point_to_add", orderEntity.getPointToAdd());
        
        return insertAction.executeAndReturnKey(params).longValue();
    }
    
    public List<OrderEntity> findByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }
}
