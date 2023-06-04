package cart.dao;

import cart.dao.entity.PointEntity;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

    public PointDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        String sql = "UPDATE point SET point = ? WHERE member_id = ?";
        jdbcTemplate.update(sql, point.getPoint(), point.getMemberId());
    }
}