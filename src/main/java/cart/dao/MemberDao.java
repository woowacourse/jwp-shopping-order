package cart.dao;

import cart.entity.MemberEntity;
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
                .usingGeneratedKeyColumns("id");
    }

    public Optional<MemberEntity> getById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        try {
            MemberEntity member = jdbcTemplate.queryForObject(sql, new MemberRowMapper(), id);
            return Optional.ofNullable(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<MemberEntity> getByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        try {
            MemberEntity member = jdbcTemplate.queryForObject(sql, new MemberRowMapper(), email);
            return Optional.ofNullable(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Long insert(MemberEntity entity) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(entity);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }

    public void update(MemberEntity entity) {
        String sql = "UPDATE member SET email = ?, password = ?, money = ?, point = ? WHERE id = ?";
        jdbcTemplate.update(sql, entity.getEmail(), entity.getPassword(), entity.getMoney(), entity.getPoint(), entity.getId());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<MemberEntity> getAll() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, new MemberRowMapper());
    }

    private static class MemberRowMapper implements RowMapper<MemberEntity> {
        @Override
        public MemberEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberEntity(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("money"),
                    rs.getInt("point")
            );
        }
    }
}
