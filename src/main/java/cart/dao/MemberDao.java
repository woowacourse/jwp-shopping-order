package cart.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<MemberEntity> rowMapper = (rs, rowNum) ->
            new MemberEntity(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("point")
            );

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public MemberEntity getMemberById(final Long id) {
        try {
            String sql = "SELECT * FROM member WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("id : " + id + " 인 회원이 존재하지 않습니다.");
        }
    }

    public MemberEntity getMemberByEmail(final String email) {
        try {
            String sql = "SELECT * FROM member WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("email : " + email + " 인 회원이 존재하지 않습니다.");
        }
    }

    public Long addMember(final MemberEntity memberEntity) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", memberEntity.getEmail())
                .addValue("password", memberEntity.getPassword())
                .addValue("point", memberEntity.getPoint());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public int updateMember(final MemberEntity memberEntity) {
        String sql = "UPDATE member SET email = ?, password = ?, point = ? WHERE id = ?";
        return jdbcTemplate.update(sql, memberEntity.getEmail(), memberEntity.getPassword(), memberEntity.getPoint(), memberEntity.getId());
    }

    public int deleteMember(final Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<MemberEntity> getAllMembers() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, rowMapper);
    }
}

