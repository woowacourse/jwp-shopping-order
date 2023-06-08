package cart.persistence.point;

import cart.domain.Point;
import cart.domain.PointHistory;
import cart.domain.repository.point.PointRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PointJdbcRepository implements PointRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<PointHistory> pointHistoryRowMapper = (rs, rowNum) ->
            new PointHistory(
                    rs.getLong("order_id"),
                    rs.getInt("earned_point"),
                    rs.getInt("used_point")
            );

    public PointJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("point_history")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long createPointHistory(Long memberId, PointHistory pointHistory) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("member_id", memberId);
        parameters.put("order_id", pointHistory.getOrderId());
        parameters.put("used_point", pointHistory.getUsedPoint());
        parameters.put("earned_point", pointHistory.getEarnedPoint());

        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public Point findPointByMemberId(Long memberId) {
        String sql = "select * from point_history where member_id = ?";
        return new Point(jdbcTemplate.query(sql, pointHistoryRowMapper, memberId));
    }

}
