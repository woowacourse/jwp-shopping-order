package cart.dao;

import cart.entity.MemberEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<MemberEntity> getMemberById(final Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, new Object[]{id}, new MemberRowMapper()));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<MemberEntity> getMemberByEmail(final String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, new Object[]{email}, new MemberRowMapper()));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void addMember(final MemberEntity member) {
        String sql = "INSERT INTO member (email, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword());
    }

    public void updateMember(final MemberEntity member) {
        String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getId());
    }

    public void deleteMember(final Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<MemberEntity> getAllMembers() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, new MemberRowMapper());
    }

    private static class MemberRowMapper implements RowMapper<MemberEntity> {
        @Override
        public MemberEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberEntity(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password")
            );
        }
    }
}

