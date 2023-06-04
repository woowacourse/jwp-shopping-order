package cart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cart.domain.Point;
import cart.entity.PointEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PointDao {

    private final RowMapper<PointEntity> rowMapper = (rs, rowNum) -> new PointEntity(
            rs.getLong("id"),
            rs.getInt("earned_point"),
            rs.getInt("left_point"),
            rs.getLong("member_id"),
            rs.getTimestamp("expired_at"),
            rs.getTimestamp("created_at")
    );

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

    public List<PointEntity> findRemainingPointsByMemberId(final Long memberId) {
        final String sql = "SELECT id, earned_point, left_point, member_id, expired_at, created_at FROM point WHERE member_id = ? AND left_point > 0 ORDER BY expired_at";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Optional<PointEntity> findById(final Long id) {
        final String sql = "SELECT id, earned_point, left_point, member_id, expired_at, created_at FROM point WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void updateLeftPoint(final Long id, final Point leftPoint) {
        final String sql = "UPDATE point SET left_point = ? WHERE id = ?";
        jdbcTemplate.update(sql, leftPoint.getValue(), id);
    }
}
