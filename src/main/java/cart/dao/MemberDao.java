package cart.dao;

import cart.domain.Member;
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

    public Optional<Member> getMemberById(final Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        final List<Member> members = this.jdbcTemplate.query(sql, new Object[]{id}, new MemberRowMapper());
        if (members.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(members.get(0));
    }

    public Optional<Member> getMemberByEmail(final String email) {
        final String sql = "SELECT * FROM member WHERE email = ?";
        final List<Member> members = this.jdbcTemplate.query(sql, new Object[]{email}, new MemberRowMapper());
        if (members.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(members.get(0));
    }

    public void addMember(final Member member) {
        final String sql = "INSERT INTO member (email, password) VALUES (?, ?)";
        this.jdbcTemplate.update(sql, member.getEmail(), member.getPassword());
    }

    public void updateMember(final Member member) {
        final String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        this.jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getId());
    }

    public void deleteMember(final Long id) {
        final String sql = "DELETE FROM member WHERE id = ?";
        this.jdbcTemplate.update(sql, id);
    }

    public List<Member> getAllMembers() {
        final String sql = "SELECT * from member";
        return this.jdbcTemplate.query(sql, new MemberRowMapper());
    }

    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new Member(rs.getLong("id"), rs.getString("email"), rs.getString("password"));
        }
    }
}

