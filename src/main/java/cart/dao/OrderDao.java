package cart.dao;

import cart.entity.OrderEntity;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final SimpleJdbcInsert insertAction;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("`order`")
                .usingGeneratedKeyColumns("id");
    }

    public OrderEntity createOrder(final OrderEntity orderEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("member_id", orderEntity.getMemberId());
        params.put("price", orderEntity.getPrice());
        final long id = insertAction.executeAndReturnKey(params).longValue();
        return new OrderEntity(id, orderEntity.getMemberId(), orderEntity.getPrice());
    }

}
