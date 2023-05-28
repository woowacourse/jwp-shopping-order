package cart.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public MemberEntity findMemberById(final Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        final List<MemberEntity> members = jdbcTemplate.query(sql, memberEntityRowMapper(), id);

        if (members.isEmpty()) {
            return null;
        }

        return members.get(0);
    }

    public MemberEntity findMemberByEmail(final String email) {
        final String sql = "SELECT * FROM member WHERE email = ?";
        final List<MemberEntity> members = jdbcTemplate.query(sql, memberEntityRowMapper(), email);

        if (members.isEmpty()) {
            return null;
        }

        return members.get(0);
    }

    public List<MemberEntity> findAllMembers() {
        final String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, memberEntityRowMapper());
    }

    public Long saveMember(final MemberEntity memberEntity) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", memberEntity.getEmail())
                .addValue("password", memberEntity.getPassword());

        return insertAction.executeAndReturnKey(parameters).longValue();
    }

    private RowMapper<MemberEntity> memberEntityRowMapper() {
        return (rs, rowNum) -> {
            final long memberId = rs.getLong("id");
            final String email = rs.getString("email");
            final String password = rs.getString("password");

            return new MemberEntity(memberId, email, password);
        };
    }
}

