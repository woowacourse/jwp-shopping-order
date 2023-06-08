package cart.dao;

import cart.entity.MemberEntity;
import cart.exception.MemberNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MemberDao {

    private static final RowMapper<MemberEntity> memberRowMapper = (rs, rn) -> new MemberEntity(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password")
    );

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public MemberEntity findById(final long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, memberRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new MemberNotFoundException();
        }
    }

    public MemberEntity findByEmail(final String email) {
        final String sql = "SELECT * FROM member WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, memberRowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            throw new MemberNotFoundException();
        }
    }

    public List<MemberEntity> findAll() {
        final String sql = "SELECT * from member ORDER BY id";
        return jdbcTemplate.query(sql, memberRowMapper);
    }
}
