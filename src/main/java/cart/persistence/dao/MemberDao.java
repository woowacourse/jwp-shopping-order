package cart.persistence.dao;

import cart.persistence.entity.MemberEntity;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private final RowMapper<MemberEntity> memberRowMapper = (result, count) ->
        new MemberEntity(result.getLong("id"),
            result.getString("name"), result.getString("password"));

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long insert(final MemberEntity memberEntity) {
        final String query = "INSERT INTO member(name, password) VALUES (?, ?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
            ps.setString(1, memberEntity.getName());
            ps.setString(2, memberEntity.getPassword());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<MemberEntity> findById(final Long id) {
        final String query = "SELECT m.id, m.name, m.password FROM member m WHERE m.id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(query, memberRowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<MemberEntity> findAll() {
        final String query = "SELECT m.id, m.name, m.password FROM member m";
        return jdbcTemplate.query(query, memberRowMapper);
    }

    public Optional<MemberEntity> findByName(final String memberName) {
        final String query = "SELECT m.id, m.name, m.password FROM member m WHERE m.name = ?";
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(query, memberRowMapper, memberName));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
