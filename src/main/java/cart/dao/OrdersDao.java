package cart.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import cart.entity.OrdersEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrdersDao {

    private final RowMapper<OrdersEntity> rowMapper = (rs, rowNum) -> new OrdersEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("point_id"),
            rs.getInt("earned_point"),
            rs.getInt("used_point"),
            rs.getTimestamp("created_at")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrdersDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    public Optional<OrdersEntity> findById(final Long id) {
        final String sql = "SELECT id, member_id, point_id, earned_point, used_point, created_at FROM orders WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
