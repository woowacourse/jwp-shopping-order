package cart.dao;

import cart.dao.entity.PointEntity;
import org.springframework.dao.EmptyResultDataAccessException;
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
                    rs.getLong("point")
            );

    public PointDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<PointEntity> findPointByMemberId(Long memberId) {
        String sql = "select * from point where member_id = ?";
        try {
            final PointEntity pointEntity = jdbcTemplate.queryForObject(sql, rowMapper, memberId);
            return Optional.ofNullable(pointEntity);
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
