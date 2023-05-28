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

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("point")
            );
        }
    }

    public Member getMemberById(final Long memberId) {
        final String sql = "SELECT id, email, password, point FROM member WHERE id = ?";
        final List<Member> members = jdbcTemplate.query(sql, new Object[]{memberId}, new MemberRowMapper());

        return members.isEmpty() ? null : members.get(0);
    }

    public Member getMemberByEmail(final String email) {
        final String sql = "SELECT id, email, password, point FROM member WHERE email = ?";
        final List<Member> members = jdbcTemplate.query(sql, new Object[]{email}, new MemberRowMapper());

        return members.isEmpty() ? null : members.get(0);
    }

    public void addMember(final Member member) {
        final String sql = "INSERT INTO member(email, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword());
    }

    public void updateMember(final Member member) {
        final String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getId());
    }

    public void deleteMember(final Long memberId) {
        final String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, memberId);
    }

    public List<Member> getAllMembers() {
        final String sql = "SELECT id, email, password, point from member";
        return jdbcTemplate.query(sql, new MemberRowMapper());
    }
}

