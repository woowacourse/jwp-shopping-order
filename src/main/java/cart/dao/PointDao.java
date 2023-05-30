package cart.dao;

import cart.entity.PointEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class PointDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertPoint;

    public PointDao(final JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.insertPoint = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("point")
                .usingGeneratedKeyColumns("id");
    }

    public int findByMemberId(final Long memberId) {
        final String sql = "SELECT point_amount FROM point WHERE member_id = :member_id";
        final Map<String, Long> parameter = Map.of("member_id", memberId);
        return namedParameterJdbcTemplate.queryForObject(sql, parameter, Integer.class);
    }

    public PointEntity insert(final PointEntity pointEntity) {
        final BeanPropertySqlParameterSource parameter = new BeanPropertySqlParameterSource(pointEntity);
        final long id = insertPoint.executeAndReturnKey(parameter).longValue();
        return new PointEntity(id, pointEntity.getPointAmount());
    }

    public void updatePoints(final PointEntity pointEntity) {
        final String sql = "UPDATE point SET point_amount = :point_amount WHERE member_id = :id;";
        final BeanPropertySqlParameterSource parameters = new BeanPropertySqlParameterSource(pointEntity);
        namedParameterJdbcTemplate.update(sql, parameters);
    }
}
