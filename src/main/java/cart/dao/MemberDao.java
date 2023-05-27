package cart.dao;

import cart.dao.entity.MemberEntity;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private static final RowMapper<MemberEntity> ROW_MAPPER = (rs, rowNum) -> new MemberEntity(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getInt("point"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
    );

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MemberEntity> getAllMembers() {
        String sql = "SELECT id, email, password, point, created_at, updated_at from member";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Optional<MemberEntity> getMemberById(Long id) {
        String sql = "SELECT id, email, password, point, created_at, updated_at FROM member WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<MemberEntity> getMemberByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, email, password, point, created_at, updated_at FROM member WHERE email = ? and password = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, ROW_MAPPER, email, password));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long addMember(MemberEntity memberEntity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO member(email, password, point) VALUES(?, ?, ?)";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    pstm.setString(1, memberEntity.getEmail());
                    pstm.setString(2, memberEntity.getPassword());
                    pstm.setInt(3, memberEntity.getPoint());
                    return pstm;
                },
                keyHolder
        );
        return (Long) keyHolder.getKeys().get("ID");
    }
}
