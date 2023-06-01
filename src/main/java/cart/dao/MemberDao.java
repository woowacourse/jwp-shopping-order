package cart.dao;

import cart.entity.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<MemberEntity> rowMapper = (resultSet, rowNum) -> new MemberEntity(
            resultSet.getLong("id"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingColumns("email", "password")
                .usingGeneratedKeyColumns("id");
    }

    public MemberEntity insert(final MemberEntity memberEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(memberEntity);
        final long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new MemberEntity(id, memberEntity.getEmail(), memberEntity.getPassword());
    }

    @Transactional(readOnly = true)
    public List<MemberEntity> findAll() {
        final String sql = "SELECT * FROM member";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Transactional(readOnly = true)
    public Optional<MemberEntity> findById(final Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
