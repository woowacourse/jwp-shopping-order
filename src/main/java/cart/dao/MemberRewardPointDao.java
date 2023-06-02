package cart.dao;

import cart.domain.point.Point;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MemberRewardPointDao {

    private static final RowMapper<Point> rowMapper = (rs, rowNum) ->
            new Point(
                    rs.getLong("id"),
                    rs.getInt("point"),
                    rs.getTimestamp("created_at")
                      .toLocalDateTime(),
                    rs.getTimestamp("expired_at")
                      .toLocalDateTime()
            );

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public MemberRewardPointDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, DataSource dataSource) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("member_reward_point")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Long memberId, Point point, Long orderId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", memberId)
                .addValue("point", point.getPoint())
                .addValue("created_at", point.getCreatedAt())
                .addValue("expired_at", point.getExpiredAt())
                .addValue("reward_order_id", orderId);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<Point> getAllByMemberId(Long memberId) {
        String sql = "SELECT * FROM member_reward_point WHERE member_id = :member_id";
        SqlParameterSource source = new MapSqlParameterSource("member_id", memberId);
        return namedParameterJdbcTemplate.query(sql, source, rowMapper);
    }

    public Point getPointByOrderId(Long orderId) {
        String sql = "SELECT * FROM member_reward_point WHERE reward_order_id = :reward_order_id";
        SqlParameterSource source = new MapSqlParameterSource("reward_order_id", orderId);
        return namedParameterJdbcTemplate.queryForObject(sql, source, rowMapper);
    }
}
