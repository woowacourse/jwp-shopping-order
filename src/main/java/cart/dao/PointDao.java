package cart.dao;

import cart.entity.PointEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PointDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<PointEntity> rowMapper = (rs, rowNum) ->
            new PointEntity(
                    rs.getLong("member_id"),
                    rs.getInt("point")
            );

    public PointDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<PointEntity> findByMemberId(Long memberId) {
        String sql = "SELECT member_id, point FROM point WHERE member_id = ? ";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, memberId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(PointEntity pointEntity) {
        String sql = "UPDATE point SET point = ? WHERE member_id = ? ";
        jdbcTemplate.update(sql, pointEntity.getPoint(), pointEntity.getMemberId());
    }
}
