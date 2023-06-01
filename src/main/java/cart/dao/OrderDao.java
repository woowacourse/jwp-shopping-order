package cart.dao;

import cart.entity.OrderEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {
    private static final RowMapper<OrderEntity> MAPPER = (resultSet, rowNum) -> new OrderEntity(
            resultSet.getLong("id"),
            resultSet.getLong("member_id"),
            resultSet.getInt("price")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("`order`")
                .usingColumns("member_id", "price")
                .usingGeneratedKeyColumns("id");
    }

    public OrderEntity createOrder(final OrderEntity orderEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("member_id", orderEntity.getMemberId());
        params.put("price", orderEntity.getPrice());
        final long id = insertAction.executeAndReturnKey(params).longValue();
        return new OrderEntity(id, orderEntity.getMemberId(), orderEntity.getPrice());
    }

    public List<OrderEntity> findByMemberId(final Long memberId) {
        final String sql = "SELECT * from `order` where member_id = ?";
        return jdbcTemplate.query(sql, MAPPER, memberId);
    }
}
