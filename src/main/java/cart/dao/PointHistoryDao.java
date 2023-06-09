package cart.dao;

import cart.dao.dto.point.PointHistoryDto;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PointHistoryDao {

    private static final RowMapper<PointHistoryDto> ROW_MAPPER = (rs, rowNum) ->
        new PointHistoryDto(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getInt("points_used"),
            rs.getInt("points_saved"),
            rs.getLong("order_id")
        );
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public PointHistoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("point_history")
            .usingGeneratedKeyColumns("id");
    }

    public void save(PointHistoryDto pointHistoryDto) {
        insertAction.execute(Map.of(
            "member_id", pointHistoryDto.getMemberId(),
            "points_used", pointHistoryDto.getPointsUsed(),
            "points_saved", pointHistoryDto.getPointsSaved(),
            "order_id", pointHistoryDto.getOrderId()
        ));
    }

    public Optional<PointHistoryDto> findByOrderId(long orderId) {
        String sql = "SELECT * from point_history WHERE order_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, orderId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

}
