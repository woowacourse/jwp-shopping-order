package cart.dao;

import cart.domain.Member;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> findById(Long id) {
        String sql = "SELECT * FROM tb_member WHERE id = ?";

        return jdbcTemplate.query(sql, new MemberRowMapper(), id)
                .stream()
                .findAny();
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM tb_member WHERE email = ?";

        return jdbcTemplate.query(sql, new MemberRowMapper(), email)
                .stream()
                .findAny();
    }

    public List<Member> findAll() {
        String sql = "SELECT * from tb_member";

        return jdbcTemplate.query(sql, new MemberRowMapper());
    }

    public void update(Member member) {
        String sql = "UPDATE tb_member SET email = ?, password = ? WHERE id = ?";

        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getId());
    }

    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(rs.getLong("id"), rs.getString("email"), rs.getString("password"));
        }
    }
}
