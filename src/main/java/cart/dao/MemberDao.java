package cart.dao;

import cart.dao.dto.MemberDto;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private static final RowMapper<MemberDto> ROW_MAPPER = (rs, rn) ->
            new MemberDto(rs.getLong("id"), rs.getString("email"), rs.getString("password"));
    private final JdbcTemplate jdbcTemplate;

    public MemberDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public MemberDto getMemberById(final Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, MemberDto.class, id);
    }

    public MemberDto getMemberByEmail(final String email) {
        final String sql = "SELECT * FROM member WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, MemberDto.class, email);
    }

    public List<MemberDto> getAllMembers() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }
}
