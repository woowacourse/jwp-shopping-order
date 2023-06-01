package cart.dao;

import cart.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {
    private static final String MEMBER_SQL = "SELECT m.id, m.email, m.password, mp.point " +
            "FROM member AS m " +
            "INNER JOIN member_point AS mp ON mp.member_id = m.id ";
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Member> rowMapper = (rs, rowNum) -> {
        long id = rs.getLong("id");
        String email = rs.getString("email");
        String password = rs.getString("password");
        long point = rs.getLong("point");
        return new Member(id, email, password, point);
    };

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Optional<Member> findById(Long id) {
        String sql = MEMBER_SQL + "WHERE m.id = ? ";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findAny();
    }

    public Optional<Member> findByEmail(String email) {
        String sql = MEMBER_SQL + "WHERE m.email = ? ";
        return jdbcTemplate.query(sql, rowMapper, email).stream().findAny();
    }

    public Long save(Member member) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("email", member.getEmail());
        mapSqlParameterSource.addValue("password", member.getPassword());
        long memberId = simpleJdbcInsert.executeAndReturnKey(mapSqlParameterSource).longValue();
        String pointSql = "INSERT INTO member_point (member_id, point) VALUES (?, ?)";
        jdbcTemplate.update(pointSql, memberId, member.getPoint().getAmount());
        return memberId;
    }

    public void updateMember(Member member) {
        String memberSql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(memberSql, member.getEmail(), member.getPassword(), member.getId());
        String pointSql = "UPDATE member_point SET point = ? where member_id = ?";
        jdbcTemplate.update(pointSql, member.getPoint().getAmount(), member.getId());
    }

    public List<Member> findAll() {
        return jdbcTemplate.query(MEMBER_SQL, rowMapper);
    }
}

