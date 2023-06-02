package cart.dao;

import cart.domain.OrderItem;
import cart.domain.Point;
import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Repository
public class PointDao {

    private final JdbcTemplate jdbcTemplate;

    public PointDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Point findByOrderId(Long orderId) {
        String sql = "select * from point where orders_id = ?";

        return jdbcTemplate.queryForObject(sql, new PointRowMapper(), orderId);
    }

    private static class PointRowMapper implements RowMapper<Point> {

        @Override
        public Point mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            int earnedPoint = rs.getInt("earned_point");
            String comment = rs.getString("comment");
            LocalDate createAt = rs.getTimestamp("create_at").toLocalDateTime().toLocalDate();
            LocalDate expiredAt = rs.getTimestamp("expired_at").toLocalDateTime().toLocalDate();
            return Point.of(id, earnedPoint, comment, createAt, expiredAt);
        }
    }
}
