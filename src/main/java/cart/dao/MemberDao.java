package cart.dao;

import cart.domain.Member;
import java.util.List;
import java.util.Optional;
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

    public Optional<Member> findMemberById(final Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        final List<Member> members = jdbcTemplate.query(sql, memberEntityRowMapper(), id);

        if (members.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(members.get(0));
    }

    public Optional<Member> findMemberByEmail(final String email) {
        final String sql = "SELECT * FROM member WHERE email = ?";
        final List<Member> members = jdbcTemplate.query(sql, memberEntityRowMapper(), email);

        if (members.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(members.get(0));
    }

    public List<Member> findAllMembers() {
        final String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, memberEntityRowMapper());
    }

    public Long saveMember(final Member memberEntity) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", memberEntity.getEmail())
                .addValue("password", memberEntity.getPassword());

        return insertAction.executeAndReturnKey(parameters).longValue();
    }

    private RowMapper<Member> memberEntityRowMapper() {
        return (rs, rowNum) -> {
            final long memberId = rs.getLong("id");
            final String email = rs.getString("email");
            final String password = rs.getString("password");

            return new Member(memberId, email, password);
        };
    }
}

