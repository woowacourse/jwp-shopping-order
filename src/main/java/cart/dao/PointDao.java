package cart.dao;

import cart.entity.PointEntity;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PointDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PointEntity> rowMapper = (rs, rowNum) -> new PointEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getInt("point")
    );

    public PointDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void update(final Long memberId, final int point) {
        String sql = "UPDATE point SET point = ? WHERE member_id = ?";

        final int affectedRow = jdbcTemplate.update(sql, point, memberId);

        if (affectedRow == 0) {
            throw new IllegalArgumentException();
        }
    }

    public Optional<PointEntity> findByMemberId(final Long memberId) {
        String sql = "SELECT * FROM point WHERE member_id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, memberId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
