package cart.dao;

import cart.domain.MemberEntity;
import cart.exception.MemberNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class MemberDao {

    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String POINT = "point";
    private static final String ALL_COLUMN = String.join(", ", ID, EMAIL, PASSWORD, POINT);
    private static final String TABLE = "member";

    private static final RowMapper<MemberEntity> rowMapper = (resultSet, rowNum) ->
            new MemberEntity(
                    resultSet.getLong(ID),
                    resultSet.getString(EMAIL),
                    resultSet.getString(PASSWORD),
                    resultSet.getInt(POINT)
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    public long insert(final MemberEntity memberEntity) {
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(memberEntity);

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public MemberEntity findById(final Long id) {
        String sql = "SELECT " + ALL_COLUMN + " FROM " + TABLE + " WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (final EmptyResultDataAccessException e) {
            throw new MemberNotFoundException();
        }
    }

    public MemberEntity findByEmail(final String email) {
        String sql = "SELECT " + ALL_COLUMN + " FROM " + TABLE + " WHERE email = ?";

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, email);
        } catch (final EmptyResultDataAccessException e) {
            throw new MemberNotFoundException();
        }
    }

    public List<MemberEntity> findAll() {
        String sql = "SELECT " + ALL_COLUMN + " from " + TABLE;

        return jdbcTemplate.query(sql, rowMapper);
    }

    public void update(final MemberEntity memberEntity) {
        String sql = "UPDATE " + TABLE + " SET email = ?, password = ?, point = ? WHERE id = ?";

        jdbcTemplate.update(sql, memberEntity.getEmail(), memberEntity.getPassword(), memberEntity.getPoint(), memberEntity.getId());
    }

    public void delete(final Long id) {
        String sql = "DELETE FROM " + TABLE + " WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }
}

