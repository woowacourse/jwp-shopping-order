package cart.persistence.dao;

import cart.exception.DatabaseException;
import cart.persistence.entity.MemberEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private static final RowMapper<MemberEntity> rowMapper =
            (rs, rowNum) ->
                    new MemberEntity(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("password")
                    );

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Long insertMember(MemberEntity memberEntity) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(memberEntity);

        try {
            return simpleJdbcInsert.executeAndReturnKey(param).longValue();
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("해당 Name(" + memberEntity.getName() + ")은 이미 사용되고 있습니다.");
        }
    }

    public Optional<MemberEntity> findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        MemberEntity memberEntity = jdbcTemplate.queryForObject(sql, rowMapper, id);

        return Optional.ofNullable(memberEntity);
    }

    public Optional<MemberEntity> findByName(String name) {
        String sql = "SELECT * FROM member WHERE name = ?";
        MemberEntity memberEntity = jdbcTemplate.queryForObject(sql, rowMapper, name);

        return Optional.ofNullable(memberEntity);
    }

    public List<MemberEntity> findAll() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void updateMember(MemberEntity memberEntity) {
        String sql = "UPDATE member SET name = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, memberEntity.getName(), memberEntity.getPassword(), memberEntity.getId());
    }

    public void deleteMember(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

