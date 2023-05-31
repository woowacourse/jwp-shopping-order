package cart.dao;

import cart.dao.entity.MemberEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberDao2 {
    private final JdbcTemplate jdbcTemplate;

    public MemberDao2(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MemberEntity> rowMapper = (rs, rowNum) -> new MemberEntity(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("nickname")
    );

    public List<MemberEntity> findAll() {
        final String sql = "SELECT * FROM member ";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
