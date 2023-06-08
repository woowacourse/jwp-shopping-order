package cart.dao;

import cart.dao.dto.member.MemberDto;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private static final RowMapper<MemberDto> ROW_MAPPER = (rs, rowNum) ->
        new MemberDto(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password")
        );
    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<MemberDto> getMemberById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<MemberDto> getMemberByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, email));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public List<MemberDto> getAllMembers() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public boolean isEmailAndPasswordExist(String email, String password) {
        String sql = "SELECT EXISTS(SELECT id FROM member WHERE email = ? AND password = ?) AS member_id_exist";
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, email, password));
        } catch (EmptyResultDataAccessException exception) {
            return false;
        }
    }
}
