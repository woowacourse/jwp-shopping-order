package cart.dao;

import cart.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (resultSet, rowNum) ->
            new Member(resultSet.getLong("id"),
                    resultSet.getString("email"),
                    resultSet.getString("password"));

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> getMemberById(final Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<Member> getMemberByEmail(final String email) {
        final String sql = "SELECT * FROM member WHERE email = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, email));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<Member> getAllMembers() {
        final String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, MEMBER_ROW_MAPPER);
    }
}

