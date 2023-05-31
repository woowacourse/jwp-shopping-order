package cart.dao;

import cart.domain.Member;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("member")
            .usingGeneratedKeyColumns("id");
    }

    public Member getMemberById(final Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        final List<Member> members = jdbcTemplate.query(sql, new Object[]{id}, new MemberRowMapper());
        return members.isEmpty() ? null : members.get(0);
    }

    public Member getMemberByEmail(final String email) {
        final String sql = "SELECT * FROM member WHERE email = ?";
        final List<Member> members = jdbcTemplate.query(sql, new Object[]{email}, new MemberRowMapper());
        return members.isEmpty() ? null : members.get(0);
    }

    public Member addMember(final Member member) {
        final Map<String, Object> params = new HashMap<>();
        params.put("email", member.getEmail());
        params.put("password", member.getPassword());
        final long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Member(id, member.getEmail(), member.getPassword());
    }

    public void updateMember(final Member member) {
        final String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getId());
    }

    public void deleteMember(final Long id) {
        final String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Member> getAllMembers() {
        final String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, new MemberRowMapper());
    }

    private static class MemberRowMapper implements RowMapper<Member> {

        @Override
        public Member mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new Member(rs.getLong("id"), rs.getString("email"), rs.getString("password"));
        }
    }
}
