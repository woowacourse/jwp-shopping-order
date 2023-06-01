package cart.persistence.dao;

import cart.persistence.entity.MemberEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingColumns("email", "password")
                .usingGeneratedKeyColumns("id");
    }

    public Optional<MemberEntity> findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        List<MemberEntity> members = jdbcTemplate.query(sql, new Object[]{id}, new MemberEntityRowMapper());
        return members.isEmpty() ? Optional.empty() : Optional.ofNullable(members.get(0));
    }

    public Optional<MemberEntity> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        List<MemberEntity> members = jdbcTemplate.query(sql, new Object[]{email}, new MemberEntityRowMapper());
        return members.isEmpty() ? Optional.empty() : Optional.ofNullable(members.get(0));
    }

    public Long add(MemberEntity MemberEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(MemberEntity);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public Long update(MemberEntity member) {
        String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getId());
        return member.getId();
    }

    public void delete(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<MemberEntity> findAll() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, new MemberEntityRowMapper());
    }

    private static class MemberEntityRowMapper implements RowMapper<MemberEntity> {
        @Override
        public MemberEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberEntity(rs.getLong("id"), rs.getString("email"), rs.getString("password"));
        }
    }
}

