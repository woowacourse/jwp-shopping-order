package cart.dao;

import cart.domain.Order;
import cart.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDao {

    public static final int PAGE_UNIT = 20;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<OrderEntity> rowMapper = (rs, rowNum) ->
            new OrderEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getInt("total_price"),
                    rs.getInt("pay_price"),
                    rs.getInt("earned_points"),
                    rs.getInt("used_points"),
                    rs.getString("order_date")
            );

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

    public OrderEntity findById(final Long id) {
        final String sql = "select id, member_id, earned_points, used_points, total_price, pay_price, order_date from orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<OrderEntity> findByIndexRange(final Long memberId, final Long index) {
        final String sql = "select id, member_id, earned_points, used_points, total_price, pay_price, order_date from orders WHERE member_id = ? ORDER BY order_date DESC, id DESC LIMIT ?, " + PAGE_UNIT;
        return jdbcTemplate.query(sql, rowMapper, memberId, index);
    }
}
