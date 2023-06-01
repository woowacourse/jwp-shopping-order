package cart.dao.point;

import cart.domain.member.Member;
import cart.domain.point.Point;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcTemplatePointDao implements PointDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplatePointDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Point> findAllAvailablePointsByMemberId(Long memberId, Timestamp boundary) {
        String sql = "SELECT point.id, point.earned_point, point.left_point, point.expired_at, point.created_at, member.id, member.email " +
                "FROM point " +
                "INNER JOIN member ON point.member_id = member.id " +
                "WHERE point.member_id = ? AND point.left_point > 0 AND point.expired_at >= ? ";

        return jdbcTemplate.query(sql, new Object[]{memberId, boundary}, (rs, row) -> {
            Long pointId = rs.getLong("point.id");
            Long earnedPoint = rs.getLong("point.earned_point");
            Long leftPoint = rs.getLong("point.left_point");
            Timestamp expiredAt = rs.getTimestamp("point.expired_at");
            Timestamp createdAt = rs.getTimestamp("point.created_at");
            String email = rs.getString("member.email");
            Member member = new Member(memberId, email, null);
            return new Point(pointId, earnedPoint, leftPoint, member, expiredAt, createdAt);
        });
    }

    @Override
    public Long createPoint(Point point) {
        String sql = "INSERT INTO point (earned_point, left_point, member_id, expired_at, created_at) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, point.getEarnedPoint());
            ps.setLong(2, point.getLeftPoint());
            ps.setLong(3, point.getMember().getId());
            ps.setTimestamp(4, point.getExpiredAt());
            ps.setTimestamp(5, point.getCreatedAt());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void updateLeftPoint(Point point) {
        String sql = "UPDATE point SET left_point = ? WHERE id = ?";
        jdbcTemplate.update(sql, point.getLeftPoint(), point.getId());
    }
}
