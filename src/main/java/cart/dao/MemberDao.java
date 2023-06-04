package cart.dao;

import cart.entity.MemberEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MemberEntity> rowMapper = (rs, rowNum) ->
            new MemberEntity(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password")
            );

    public MemberEntity findById(Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public MemberEntity findByEmail(String email) {
        final String sql = "SELECT * FROM member WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, email);
    }

    public List<MemberEntity> findAll() {
        final String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void save(MemberEntity memberEntity) {
        final String sql = "INSERT INTO member (email, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, memberEntity.getEmail(), memberEntity.getPassword());
    }

    public void update(MemberEntity memberEntity) {
        final String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, memberEntity.getEmail(), memberEntity.getPassword(), memberEntity.getId());
    }

    public void delete(Long id) {
        final String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
