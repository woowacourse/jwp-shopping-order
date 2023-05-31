package cart.auth.dao;

import cart.exception.AuthorizationException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AuthDao {

    private final JdbcTemplate jdbcTemplate;

    public AuthDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Long findIdByEmailAndPassword(final String email, final String password) {
        final String sql = "select id from member where email = ? and password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, email, password);
        } catch (DataAccessException e) {
            throw new AuthorizationException("잘못된 인증 정보입니다.");
        }
    }
}
