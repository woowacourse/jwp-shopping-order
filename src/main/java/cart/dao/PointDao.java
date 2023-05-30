package cart.dao;

import cart.domain.Member;
import cart.domain.Point;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PointDao {
    private final JdbcTemplate jdbcTemplate;

    public PointDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(Member member, Point point) {
        String sql = "INSERT INTO point (member_id, amount) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getId(), point.getValue());
    }

    public Optional<Point> findByMember(Member member) {
        String sql = "SELECT * FROM point WHERE member_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new PointRowMapper(), member.getId()));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static class PointRowMapper implements RowMapper<Point> {

        @Override
        public Point mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Point(rs.getInt("amount"));
        }
    }
}
