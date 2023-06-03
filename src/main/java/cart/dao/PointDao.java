package cart.dao;

import cart.dao.entity.PointEntity;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PointDao {

    private static final RowMapper<PointEntity> ROW_MAPPER = (rs, rowNum) ->
        new PointEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getInt("point")
        );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public PointDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("point")
            .usingGeneratedKeyColumns("id");
    }

    public long save(PointEntity pointEntity) {
        Number generatedKey = insertAction.executeAndReturnKey(Map.of(
            "member_id", pointEntity.getMemberId(),
            "point", pointEntity.getPoint()
        ));
        return generatedKey.longValue();
    }

    public Optional<PointEntity> findByMemberId(long memberId) {
        String sql = "SELECT * from point WHERE member_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, memberId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void update(PointEntity point) {
        String sql = "UPDATE point SET point = ? WHERE id = ?";
        jdbcTemplate.update(sql, point.getPoint(), point.getId());
    }
}