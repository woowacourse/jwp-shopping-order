package cart.dao;

import cart.entity.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private static final RowMapper<MemberEntity> rowMapper = (rs, rowNum) -> {
        long id = rs.getLong("id");
        String email = rs.getString("email");
        String password = rs.getString("password");
        return new MemberEntity(id, email, password);
    };
    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<MemberEntity> getMemberById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        try {
            MemberEntity memberEntity = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(memberEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<MemberEntity> getMemberByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        try {
            MemberEntity memberEntity = jdbcTemplate.queryForObject(sql, rowMapper, email);
            return Optional.ofNullable(memberEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<MemberEntity> getAllMembers() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, rowMapper);
    }
}

