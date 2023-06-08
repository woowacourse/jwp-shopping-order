package cart.dao;

import cart.dao.entity.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private static final RowMapper<MemberEntity> MEMBER_ROW_MAPPER = (resultSet, rowNum) ->
            new MemberEntity(resultSet.getLong("id"),
                    resultSet.getString("email"),
                    resultSet.getString("password"));

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<MemberEntity> findById(final Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<MemberEntity> findByEmail(final String email) {
        final String sql = "SELECT * FROM member WHERE email = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, email));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<MemberEntity> findAll() {
        final String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, MEMBER_ROW_MAPPER);
    }
}

