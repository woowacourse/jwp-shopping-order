package cart.dao;

import cart.domain.Member;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

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

    public Optional<Member> findById(Long id) {
        String sql = "SELECT member.id, member.email, member.password, member_point.point " +
                "FROM member " +
                "JOIN member_point ON member_point.member_id = member.id " +
                "WHERE member.id = ? ";
        return jdbcTemplate.query(sql, new MemberRowMapper(), id).stream().findAny();
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT member.id, member.email, member.password, member_point.point " +
                "FROM member " +
                "JOIN member_point ON member_point.member_id = member.id " +
                "WHERE member.email = ? ";
        return jdbcTemplate.query(sql, new MemberRowMapper(), email).stream().findAny();
    }

    public Long save(Member member) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("email", member.getEmail());
        mapSqlParameterSource.addValue("password", member.getPassword());
        return simpleJdbcInsert.executeAndReturnKey(mapSqlParameterSource).longValue();
    }

    public void updateMember(Member member) {
        String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getId());
    }

    public void deleteMember(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Member> findAll() {
        String sql = "SELECT member.id, member.email, member.password, member_point.point " +
                "FROM member " +
                "JOIN member_point ON member_point.member_id = member.id ";

        return jdbcTemplate.query(sql, new MemberRowMapper());
    }

    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(rs.getLong("member.id"), rs.getString("member.email"), rs.getString("member.password"), rs.getLong("member_point.point"));
        }
    }
}

