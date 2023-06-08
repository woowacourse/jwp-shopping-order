package cart.dao;

import cart.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MemberDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Member findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        List<Member> members = jdbcTemplate.query(sql, params, new MemberRowMapper());
        return members.isEmpty() ? null : members.get(0);
    }

    public Member findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = :email";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", email);
        List<Member> members = jdbcTemplate.query(sql, params, new MemberRowMapper());
        return members.isEmpty() ? null : members.get(0);
    }

    public Long insert(Member member) {
        String sql = "INSERT INTO member (email, password, point) VALUES (:email, :password, :point)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", member.getEmail());
        params.addValue("password", member.getPassword());
        params.addValue("point", member.getPointAsInt());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, params, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public void update(Member member) {
        String sql = "UPDATE member SET email = :email, password = :password, point = :point WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", member.getEmail());
        params.addValue("password", member.getPassword());
        params.addValue("point", member.getPointAsInt());
        params.addValue("id", member.getId());

        jdbcTemplate.update(sql, params);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM member WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbcTemplate.update(sql, params);
    }

    public List<Member> findAll() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, new MemberRowMapper());
    }

    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(rs.getLong("id"), rs.getString("email"), rs.getString("password"), rs.getInt("point"));
        }
    }
}

