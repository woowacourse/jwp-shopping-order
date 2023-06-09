package cart.dao;

import cart.entity.PointHistoryEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PointHistoryDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<PointHistoryEntity> rowMapper = (rs, rowNum) ->
            new PointHistoryEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getLong("points_used"),
                    rs.getLong("points_saved"),
                    rs.getLong("order_id")
            );

    public PointHistoryDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("point_history")
                .usingGeneratedKeyColumns("id");
    }

    public void save(final PointHistoryEntity pointHistoryEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("member_id", pointHistoryEntity.getMemberId());
        params.put("points_used", pointHistoryEntity.getPointsUsed());
        params.put("points_saved", pointHistoryEntity.getPointsSaved());
        params.put("order_id", pointHistoryEntity.getOrderId());

        insertAction.execute(params);
    }

    public Optional<PointHistoryEntity> findByOrderId(final Long orderId) {
        final String sql = "SELECT * FROM point_history WHERE order_id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, orderId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
