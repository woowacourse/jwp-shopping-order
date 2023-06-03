package cart.persistence.dao;

import cart.persistence.entity.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public long create(final MemberEntity member) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(member);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public Optional<MemberEntity> findMemberById(final long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        try {
            MemberEntity member = jdbcTemplate.queryForObject(sql, RowMapperHelper.memberRowMapper(), id);
            return Optional.of(member);
        } catch (IncorrectResultSizeDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<MemberEntity> findMemberByEmail(final String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        try {
            MemberEntity member = jdbcTemplate.queryForObject(sql, RowMapperHelper.memberRowMapper(), email);
            return Optional.of(member);
        } catch (IncorrectResultSizeDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<MemberEntity> findAll() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, RowMapperHelper.memberRowMapper());
    }

    public void update(final MemberEntity member) {
        String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getId());
    }

    public void deleteById(final long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

