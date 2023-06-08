package cart.db.dao;

import cart.db.entity.MemberEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MemberEntity getMemberById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        List<MemberEntity> members = jdbcTemplate.query(sql, new Object[]{id}, new MemberDao.MemberRowMapper());
        return members.isEmpty() ? null : members.get(0);
    }

    public MemberEntity getMemberByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        List<MemberEntity> members = jdbcTemplate.query(sql, new Object[]{email}, new MemberDao.MemberRowMapper());
        return members.isEmpty() ? null : members.get(0);
    }

    public void addMember(MemberEntity member) {
        String sql = "INSERT INTO member (email, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword());
    }

    public void updateMember(MemberEntity member) {
        String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getId());
    }

    public void deleteMember(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<MemberEntity> getAllMembers() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, new MemberDao.MemberRowMapper());
    }

    private static class MemberRowMapper implements RowMapper<MemberEntity> {
        @Override
        public MemberEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberEntity(rs.getLong("id"), rs.getString("email"), rs.getString("password"));
        }
    }
}
