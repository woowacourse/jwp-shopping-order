package cart.dao;

import cart.domain.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "earned_points", "used_points", "total_price", "pay_price");
    }

    public Long insert(final Order order) {
        final Map<String, Object> params = new HashMap<>();
        params.put("member_id", order.getMemberId());
        params.put("earned_points", order.getEarnedPoints());
        params.put("used_points", order.getUsedPoints());
        params.put("total_price", order.getTotalPrice());
        params.put("pay_price", order.getPayPrice());

        return insertAction.executeAndReturnKey(params).longValue();
    }
}
