package cart.dao;

import cart.entity.PointEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Repository
public class PointDao {

    private final JdbcTemplate jdbcTemplate;

    public PointDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PointEntity findBy(Long memberId, Long orderId) {
        String sql = "select id, earned_point, comment, create_at, expired_at from point where member_id = ? and orders_id = ?";

        return jdbcTemplate.queryForObject(sql, new PointRowMapper(), memberId, orderId);
    }

    public List<PointEntity> findByMemberId(Long memberId) {
        String sql = "select id, earned_point, comment, create_at, expired_at from point where member_id = ?";

        return jdbcTemplate.query(sql, new PointRowMapper(), memberId);
    }

    public Long save(Long memberId, Long orderId, PointEntity pointEntity) {
        String sql = "insert into point(member_id, orders_id, earned_point, comment, expired_at) values(?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, memberId);
            ps.setLong(2, orderId);
            ps.setInt(3, pointEntity.getValue());
            ps.setString(4, pointEntity.getComment());
            ps.setTimestamp(5, Timestamp.valueOf(pointEntity.getExpiredAt().atStartOfDay()));
            return ps;
        }, keyHolder);

        return (Long) keyHolder.getKeys().get("ID");
    }

    public void delete(Long memberId, Long orderId) {
        String sql = "delete from point where member_id = ? and orders_id = ?";

        jdbcTemplate.update(sql, memberId, orderId);
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
