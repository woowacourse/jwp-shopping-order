package cart.dao;

import cart.entity.MemberEntity;
import cart.exception.MemberNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static cart.entity.RowMapperUtil.memberEntityRowMapper;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public MemberEntity findById(final long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, memberEntityRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new MemberNotFoundException();
        }
    }

    public MemberEntity findByEmail(final String email) {
        final String sql = "SELECT * FROM member WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, memberEntityRowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            throw new MemberNotFoundException();
        }
    }

    public List<MemberEntity> findAll() {
        final String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, memberEntityRowMapper);
    }
}
