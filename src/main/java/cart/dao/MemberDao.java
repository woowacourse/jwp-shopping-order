package cart.dao;

import cart.entity.MemberEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertMember;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertMember = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public MemberEntity getMemberById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        List<MemberEntity> members = jdbcTemplate.query(sql, new Object[]{id}, new MemberRowMapper());
        return members.isEmpty() ? null : members.get(0);
    }

    public MemberEntity getMemberByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        List<MemberEntity> members = jdbcTemplate.query(sql, new Object[]{email}, new MemberRowMapper());
        return members.isEmpty() ? null : members.get(0);
    }

    public MemberEntity addMember(MemberEntity memberEntity) {
        final Map<String, String> parameters = Map.of(
                "email", memberEntity.getEmail(),
                "password", memberEntity.getPassword());

        final long id = insertMember.executeAndReturnKey(parameters).longValue();
        return new MemberEntity(id, memberEntity.getEmail(), memberEntity.getPassword());
    }

    public void updateMember(MemberEntity memberEntity) {
        String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, memberEntity.getEmail(), memberEntity.getPassword(), memberEntity.getId());
    }

    public void deleteMember(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<MemberEntity> getAllMembers() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, new MemberRowMapper());
    }

    private static class MemberRowMapper implements RowMapper<MemberEntity> {
        @Override
        public MemberEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberEntity(rs.getLong("id"), rs.getString("email"), rs.getString("password"));
        }
    }
}

