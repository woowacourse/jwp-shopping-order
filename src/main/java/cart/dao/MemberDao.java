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

    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Member.of(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("points")
            );
        }
    }

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member findMemberById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        List<Member> members = jdbcTemplate.query(sql, new MemberRowMapper(), id);
        return members.isEmpty() ? null : members.get(0);
    }

    public Member findMemberByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        List<Member> members = jdbcTemplate.query(sql, new MemberRowMapper(), email);
        return members.isEmpty() ? null : members.get(0);
    }

    public void addMember(Member member) {
        String sql = "INSERT INTO member (email, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword());
    }

    public void updateMember(Member member) {
        String sql = "UPDATE member SET email = ?, password = ?, points = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getPoints(), member.getId());
    }

    public void deleteMember(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Member> findAllMembers() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, new MemberRowMapper());
    }
}

