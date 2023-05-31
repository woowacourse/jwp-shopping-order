package cart.dao;

import cart.dao.entity.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id")
                .usingColumns("email", "password", "point");
    }

    public List<MemberEntity> getAllMembers() {
        String sql = "SELECT id, email, password, point, created_at, updated_at from member";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Optional<MemberEntity> getMemberById(Long id) {
        String sql = "SELECT id, email, password, point, created_at, updated_at FROM member WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<MemberEntity> getMemberByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, email, password, point, created_at, updated_at FROM member WHERE email = ? and password = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, email, password));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long addMember(MemberEntity memberEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(memberEntity);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public void update(MemberEntity memberEntity) {
        String sql = "UPDATE member set email = ?, password = ?, point = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                memberEntity.getEmail(), memberEntity.getPassword(), memberEntity.getPoint(), memberEntity.getId()
        );
    }
}
