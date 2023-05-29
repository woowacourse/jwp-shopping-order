package cart.dao;

import cart.domain.member.Member;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingColumns("email", "password", "point")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getInt("point")
    );

    public Long insert(final Member member) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", member.getEmailValue())
                .addValue("password", member.getPasswordValue());

        return jdbcInsert.executeAndReturnKey(params).longValue();
    }

    public Optional<Member> findById(final Long id) {
        final String sql = "SELECT id, email, password, point FROM member WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberRowMapper, id));
        } catch (final DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findByEmail(final String email) {
        final String sql = "SELECT id, email, password, point FROM member WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberRowMapper, email));
        } catch (final DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(final Member member) {
        final String sql = "UPDATE member SET email = ?, password = ?, point = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmailValue(), member.getPasswordValue(), member.getPointValue(), member.getId());
    }

    public void delete(final Long id) {
        final String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Member> findAll() {
        final String sql = "SELECT id, email, password, point from member";
        return jdbcTemplate.query(sql, memberRowMapper);
    }
}

