package cart.dao;

import java.util.HashMap;
import java.util.Map;

import cart.entity.OrdersEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrdersDao {

    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrdersDao(final JdbcTemplate jdbcTemplate) {
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public OrdersEntity insert(final OrdersEntity ordersEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("member_id", ordersEntity.getMemberId());
        params.put("point_id", ordersEntity.getPointId());
        params.put("earned_point", ordersEntity.getEarnedPoint());
        params.put("used_point", ordersEntity.getUsedPoint());
        params.put("created_at", ordersEntity.getCreatedAt());

        final long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new OrdersEntity(id, ordersEntity);
    }
}
