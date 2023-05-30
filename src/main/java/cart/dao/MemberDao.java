package cart.dao;

import cart.domain.member.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberDao {

    private static final RowMapper<Member> MEMBER_MAPPER = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getInt("point")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Member getMemberById(final Long id) {
        final String sql = "SELECT id, email, password, point FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, MEMBER_MAPPER, id);
    }

    public Member getMemberByEmail(final String email) {
        final String sql = "SELECT id, email, password, point FROM member WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, MEMBER_MAPPER, email);
    }

    public void addMember(final Member member) {
        final SqlParameterSource source = new MapSqlParameterSource()
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword());
        jdbcInsert.execute(source);
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
        final String sql = "SELECT id, email, password, point from member";
        return jdbcTemplate.query(sql, MEMBER_MAPPER);
    }

    public void updatePoint(final Member member) {
        final String sql = "UPDATE member SET point = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getPoint(), member.getId());
    }
}
