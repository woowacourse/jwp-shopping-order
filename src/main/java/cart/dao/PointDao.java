package cart.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PointDao {

    private final JdbcTemplate jdbcTemplate;

    public PointDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long selectByMemberId(final long memberId) {
        String sql = "SELECT point FROM point WHERE member_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, memberId);
    }

    public void update(final long memberId, final long point) {
        String sql = "UPDATE point SET point = ? WHERE member_id = ?";
        jdbcTemplate.update(sql, point, memberId);
    }
}
