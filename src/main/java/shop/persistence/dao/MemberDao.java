package shop.persistence.dao;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import shop.exception.DatabaseException;
import shop.persistence.entity.MemberEntity;

import java.util.List;

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
            throw new DatabaseException.DataConflictException("해당 Name(" + memberEntity.getName() +
                    ")은 이미 사용되고 있습니다.");
        }
    }

    public MemberEntity findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DatabaseException.IllegalDataException(id + "를 갖는 회원을 찾을 수 없습니다.");
        }
    }

    public MemberEntity findByName(String name) {
        String sql = "SELECT * FROM member WHERE name = ?";

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, name);
        } catch (EmptyResultDataAccessException e) {
            throw new DatabaseException.IllegalDataException(name + "을 갖는 회원을 찾을 수 없습니다.");
        }
    }

    public List<MemberEntity> findAll() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Boolean isExistByName(String name) {
        String sql = "SELECT EXISTS (SELECT * FROM member WHERE name = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }
}

