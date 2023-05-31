package cart.dao;

import java.util.HashMap;
import java.util.Map;

import cart.domain.Point;
import cart.entity.PointEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PointDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public PointDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("point")
                .usingGeneratedKeyColumns("id");
    }

    public PointEntity insert(final PointEntity pointEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("earned_point", pointEntity.getEarnedPoint());
        params.put("left_point", pointEntity.getLeftPoint());
        params.put("member_id", pointEntity.getMemberId());
        params.put("expired_at", pointEntity.getExpiredAt());
        params.put("created_at", pointEntity.getCreatedAt());

        final long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new PointEntity(id, pointEntity);
    }

    public Point findLeftPointByMemberId(final Long memberId) {
        final String sql = "SELECT SUM(left_point) as total FROM point WHERE member_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> Point.valueOf(rs.getInt("total")), memberId);
    }
}
