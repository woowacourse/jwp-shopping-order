package cart.db.dao;

import cart.db.entity.MemberEntity;
import cart.domain.member.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;


    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Long create(final MemberEntity memberEntity) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(memberEntity);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public List<MemberEntity> findAll() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, new MemberEntityRowMapper());
    }

    public MemberEntity findById(final Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        List<MemberEntity> members = jdbcTemplate.query(sql, new Object[]{id}, new MemberEntityRowMapper());
        return members.isEmpty() ? null : members.get(0);
    }

    public MemberEntity findByName(final String name) {
        String sql = "SELECT * FROM member WHERE name = ?";
        List<MemberEntity> members = jdbcTemplate.query(sql, new Object[]{name}, new MemberEntityRowMapper());
        return members.isEmpty() ? null : members.get(0);
    }

    public void update(final Member member) {
        String sql = "UPDATE member SET name = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getName(), member.getPassword(), member.getId());
    }

    public void delete(final Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Boolean existsByMember(final MemberEntity memberEntity) {
        String sql = "SELECT EXISTS(SELECT * FROM member WHERE name = ? AND password = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, memberEntity.getName(), memberEntity.getPassword());
    }

    public Boolean existsByName(final String name) {
        String sql = "SELECT EXISTS(SELECT * FROM member WHERE name = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }

    private static class MemberEntityRowMapper implements RowMapper<MemberEntity> {
        @Override
        public MemberEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberEntity(rs.getLong("id"), rs.getString("name"), rs.getString("password"));
        }
    }
}

