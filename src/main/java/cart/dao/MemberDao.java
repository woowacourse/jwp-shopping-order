package cart.dao;

import cart.domain.member.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;


    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Member getMemberById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        List<Member> members = jdbcTemplate.query(sql, new Object[]{id}, new MemberRowMapper());
        return members.isEmpty() ? null : members.get(0);
    }

    public Member getMemberByName(String name) {
        String sql = "SELECT * FROM member WHERE name = ?";
        List<Member> members = jdbcTemplate.query(sql, new Object[]{name}, new MemberRowMapper());
        return members.isEmpty() ? null : members.get(0);
    }

    public Long addMember(Member member) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", member.getName());
        params.put("password", member.getPassword());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public void updateMember(Member member) {
        String sql = "UPDATE member SET name = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getName(), member.getPassword(), member.getId());
    }

    public void deleteMember(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Member> getAllMembers() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, new MemberRowMapper());
    }

    public Boolean existsByMember(final Member member) {
        String sql = "SELECT EXISTS(SELECT * FROM member WHERE name = ? AND password = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, member.getName(), member.getPassword());
    }

    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(rs.getLong("id"), rs.getString("name"), rs.getString("password"));
        }
    }
}

