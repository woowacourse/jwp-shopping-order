package cart.dao;

import cart.domain.Point;
import cart.entity.OrderEntity;
import cart.entity.PointEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class PointDao {

    private final JdbcTemplate jdbcTemplate;

    public PointDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PointEntity findByOrderId(Long orderId) {
        String sql = "select id, earned_point, comment, create_at, expired_at from point where orders_id = ?";

        return jdbcTemplate.queryForObject(sql, new PointRowMapper(), orderId);
    }

    public List<PointEntity> findByMemberId(Long memberId) {
        String sql = "select id, earned_point, comment, create_at, expired_at from point where member_id = ?";

        return jdbcTemplate.query(sql, new PointRowMapper(), memberId);
    }

    private static class PointRowMapper implements RowMapper<PointEntity> {

        @Override
        public PointEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            int earnedPoint = rs.getInt("earned_point");
            String comment = rs.getString("comment");
            LocalDate createAt = rs.getTimestamp("create_at").toLocalDateTime().toLocalDate();
            LocalDate expiredAt = rs.getTimestamp("expired_at").toLocalDateTime().toLocalDate();

            return new PointEntity(id, earnedPoint, comment, createAt, expiredAt);
        }
    }

}
