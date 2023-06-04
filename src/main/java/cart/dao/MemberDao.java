package cart.dao;

import cart.entity.MemberEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<MemberEntity> rowMapper = (rs, rowNum) ->
            new MemberEntity(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("point")
            );

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MemberEntity> findAll() {
        final String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public MemberEntity findById(Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public MemberEntity findByEmail(String email) {
        final String sql = "SELECT * FROM member WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, email);
    }

    public void updatePoint(MemberEntity memberEntity) {
        final String sql = "UPDATE member SET point = ? WHERE id = ?";
        jdbcTemplate.update(sql, memberEntity.getPoint(), memberEntity.getId());
    }
}
