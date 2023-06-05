package cart.dao;

import cart.entity.MemberEntity;
import cart.entity.MemberInfoEntity;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private static final RowMapper<MemberEntity> memberRowMapper = (rs, rowNum) -> {
        long id = rs.getLong("id");
        String email = rs.getString("email");
        String password = rs.getString("password");
        return new MemberEntity(id, email, password);
    };

    private static final RowMapper<MemberInfoEntity> memberInfoRowMapper = (rs, rowNum) -> {
        long id = rs.getLong("id");
        String email = rs.getString("email");
        return new MemberInfoEntity(id, email);
    };

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(MemberEntity member) {
        String sql = "INSERT INTO member (email, password) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, member.getEmail());
            ps.setString(2, member.getPassword());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<MemberEntity> findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        try {
            MemberEntity memberEntity = jdbcTemplate.queryForObject(sql, memberRowMapper, id);
            return Optional.ofNullable(memberEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<MemberInfoEntity> findMemberInfoById(Long id) {
        String sql = "SELECT id, email FROM member WHERE id = ?";
        try {
            MemberInfoEntity memberEntity = jdbcTemplate.queryForObject(sql, memberInfoRowMapper, id);
            return Optional.ofNullable(memberEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<MemberEntity> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        try {
            MemberEntity memberEntity = jdbcTemplate.queryForObject(sql, memberRowMapper, email);
            return Optional.ofNullable(memberEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<MemberInfoEntity> findMemberInfoByEmail(String email) {
        String sql = "SELECT id, email FROM member WHERE email = ?";
        try {
            MemberInfoEntity memberEntity = jdbcTemplate.queryForObject(sql, memberInfoRowMapper, email);
            return Optional.ofNullable(memberEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<MemberEntity> findAll() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, memberRowMapper);
    }
}

