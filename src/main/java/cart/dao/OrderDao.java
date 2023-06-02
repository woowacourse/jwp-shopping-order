package cart.dao;

import cart.entity.OrderEntity;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<OrderEntity> orderRowMapper = (rs, rowNum) -> new OrderEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getTimestamp("created_at")
    );

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id");
    }

    public Long save(final OrderEntity orderEntity) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("member_id", orderEntity.getMemberId());

        return insertAction.executeAndReturnKey(parameters).longValue();
    }

    public List<OrderEntity> findOrderByMemberId(final Long memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ?";

        try {
            return jdbcTemplate.query(sql, orderRowMapper, memberId);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public Optional<OrderEntity> findById(long id) {
        String sql = "SELECT * from orders WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, orderRowMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
