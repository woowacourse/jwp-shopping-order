package cart.dao;

import cart.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Member> memberRowMapper = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password")
    );

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member getMemberById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        List<Member> members = jdbcTemplate.query(sql, new Object[]{id}, memberRowMapper);
        return members.isEmpty() ? null : members.get(0);
    }

    public Member getMemberByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        List<Member> members = jdbcTemplate.query(sql, new Object[]{email}, memberRowMapper);
        return members.isEmpty() ? null : members.get(0);
    }


    public List<Member> getAllMembers() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, memberRowMapper);
    }

}

