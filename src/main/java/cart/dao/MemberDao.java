package cart.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cart.domain.Member;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> rowMapper = (rs, rowNum) -> {
        final long id = rs.getLong("id");
        final String email = rs.getString("email");
        final String password = rs.getString("password");
        final int grade = rs.getInt("grade");
        return new Member(id, email, password, grade);
    };

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member getMemberById(Long id) {
        String sql = "SELECT * FROM member AS m "
                + "JOIN grade AS g "
                + "ON m.id = g.id "
                + "WHERE m.id = ?";
        List<Member> members = jdbcTemplate.query(sql, rowMapper, id);
        return members.isEmpty() ? null : members.get(0);
    }

    public Member getMemberByEmail(String email) {
        String sql = "SELECT * FROM member AS m "
                + "JOIN grade AS g "
                + "ON m.id = g.id "
                + "WHERE m.email = ?";
        List<Member> members = jdbcTemplate.query(sql, rowMapper, email);
        return members.isEmpty() ? null : members.get(0);
    }

    public List<Member> getAllMembers() {
        String sql = "SELECT * FROM member AS m "
                + "JOIN grade AS g "
                + "ON m.id = g.id";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
