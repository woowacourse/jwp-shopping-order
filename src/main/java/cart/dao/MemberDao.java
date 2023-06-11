package cart.dao;

import cart.domain.Member;
import cart.domain.Point;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> memberRowMapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        String email = rs.getString("email");
        String password = rs.getString("password");
        Point point = new Point(rs.getLong("point"));
        return new Member(id, email, password, point);
    };

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> getMemberById(Long id) {
        String sql = "SELECT id, email, password, point FROM member WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberRowMapper, id));
    }

    public Optional<Member> getMemberByEmail(String email) {
        String sql = "SELECT id, email, password, point FROM member WHERE email = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberRowMapper, email));
    }

    public Optional<Member> findByOrderId(Long id) {
        String sql = "SELECT m.id, m.email, m.password, m.point " +
                "FROM member m " +
                "JOIN shopping_order so ON m.id = so.member_id " +
                "WHERE so.id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberRowMapper, id));
    }

    public List<Member> getAllMembers() {
        String sql = "SELECT id, email, password, point from member";
        return jdbcTemplate.query(sql, memberRowMapper);
    }

    public void addMember(Member member) {
        String sql = "INSERT INTO member (email, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword());
    }

    public void updateMember(Member member) {
        String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getId());
    }

    public void deleteMember(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updatePoints(Long point, Member member) {
        String sql = "UPDATE member SET point = ? WHERE id = ?";
        jdbcTemplate.update(sql, point, member.getId());
    }

    public Point findPoints(Member member) {
        String sql = "SELECT point FROM member WHERE id = ?";
        return new Point(jdbcTemplate.queryForObject(sql, Long.class, member.getId()));
    }
}
