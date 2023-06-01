package cart.dao;

import cart.auth.Credential;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
public class CredentialDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Credential> rowMapper = (resultSet, rowNum) -> new Credential(
            resultSet.getLong("id"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );

    public CredentialDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Credential> findByEmail(final String email) {
        final String sql = "SELECT * FROM member WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, email));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
