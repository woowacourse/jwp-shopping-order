package cart.dao;

import cart.entity.PointEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class PointDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<PointEntity> rowMapper = (rs, rowNum) ->
            new PointEntity(
                    rs.getLong("member_id"),
                    rs.getInt("point")
            );

    public PointDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
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
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("point", pointEntity.getPoint());
        params.addValue("member_id", pointEntity.getMemberId());

        String sql = "UPDATE point SET point = :point WHERE member_id = :member_id ";
        namedParameterJdbcTemplate.update(sql, params);
    }
}
