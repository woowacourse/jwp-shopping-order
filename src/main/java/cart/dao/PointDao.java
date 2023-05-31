package cart.dao;

import cart.domain.Member;
import cart.domain.Point;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public class PointDao {

    private final JdbcTemplate jdbcTemplate;

    public PointDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Point> getBeforeExpirationAndRemainingPointsByMemberId(Long memberId) {
        String sql = "SELECT point.id, point.earned_point, point.left_point, point.member_id, member.email, point.expired_at, point.created_at " +
                "FROM point " +
                "INNER JOIN member ON point.member_id = member.id " +
                "WHERE point.member_id = ? AND point.expired_at >= NOW() AND point.left_point > 0 " +
                "ORDER BY point.expired_at";
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            Long id = rs.getLong("point.id");
            int earnedPoint = rs.getInt("earned_point");
            int leftPoint = rs.getInt("left_point");
            LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
            LocalDateTime expiredAt = rs.getTimestamp("expired_at").toLocalDateTime();
            String email = rs.getString("email");
            Member member = new Member(memberId, email, null);
            return new Point(id, earnedPoint, leftPoint, member, expiredAt, createdAt);
        });
    }

    public Long createPoint(Point point) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO point (earned_point, left_point, member_id, expired_at, created_at) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setInt(1, point.getEarnedPoint());
            ps.setInt(2, point.getEarnedPoint());
            ps.setLong(3, point.getMember().getId());
            ps.setTimestamp(4, Timestamp.valueOf(point.getExpiredAt()));
            ps.setTimestamp(5, Timestamp.valueOf(point.getCreatedAt()));

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void updatePointLeftBalance(Point point, int leftBalance) {
        String sql = "UPDATE point SET left_point = ? WHERE id = ?";
        jdbcTemplate.update(sql, leftBalance, point.getId());
    }
}
