package cart.dao;

import cart.domain.member.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingColumns("email", "password", "point")
                .usingGeneratedKeyColumns("id");
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

   /* public Long addMember(final Member member) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", member.getEmailValue())
                .addValue("password", member.getPasswordValue());

        return jdbcInsert.executeAndReturnKey(params).longValue();
    }*/

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

    public void updateMember(final Member member) {
        final String sql = "UPDATE member SET email = ?, password = ?, point = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmailValue(), member.getPasswordValue(), member.getPointValue(), member.getId());
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

