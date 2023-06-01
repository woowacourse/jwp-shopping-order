package cart.persistence.point;

import cart.application.repository.PointRepository;
import cart.domain.Member;
import cart.domain.Point;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PointJdbcRepository implements PointRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

//    private final RowMapper<Point> pointRowMapper = (rs, rowNum) ->
//            new Point(
//                    rs.getLong("id"),
//                    rs.getLong("member_id"),
//                    rs.getInt("point")
//            );

    public PointJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("point")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Point findPointByMember(final Member member) {
        return null;
    }
//
//    public Point findByMemberId(final Long id) {
//        final String sql = "select * from point where member_id = ?";
//        return jdbcTemplate.queryForObject(sql, , id);
//    }
}
