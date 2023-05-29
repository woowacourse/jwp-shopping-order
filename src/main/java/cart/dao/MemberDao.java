package cart.dao;

import cart.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member getMemberById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        List<Member> members = jdbcTemplate.query(sql, new Object[]{id}, new MemberRowMapper());
        return members.isEmpty() ? null : members.get(0);
    }

    public Member getMemberByName(String name) {
        String sql = "SELECT * FROM member WHERE name = ?";
        List<Member> members = jdbcTemplate.query(sql, new Object[]{name}, new MemberRowMapper());
        return members.isEmpty() ? null : members.get(0);
    }

    public void addMember(Member member) {
        String sql = "INSERT INTO member (name, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getName(), member.getPassword());
    }

    public void updateMember(Member member) {
        String sql = "UPDATE member SET name = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getName(), member.getPassword(), member.getId());
    }

    public void deleteMember(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Member> getAllMembers() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, new MemberRowMapper());
    }

    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(rs.getLong("id"), rs.getString("name"), rs.getString("password"));
        }
    }
}

