package cart.dao;

import cart.domain.Member;
import cart.entity.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<MemberEntity> rowMapper = (rs, rowNum) ->
            MemberEntity.of(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password")
            );

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<MemberEntity> findById(final Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<MemberEntity> findByEmail(final String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, email));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void addMember(Member member) {
        String sql = "INSERT INTO member (email, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword());
    }

    public void updateMember(Member member) {
        String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getId());
    }

    public void deleteMember(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<MemberEntity> findAll() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, rowMapper);
    }
}

