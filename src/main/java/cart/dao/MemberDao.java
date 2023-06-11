package cart.dao;

import cart.dao.entity.MemberEntity;
import cart.domain.member.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<MemberEntity> memberEntityRowMapper = ((rs, rowNum) ->
            new MemberEntity.Builder()
                    .id(rs.getLong("id"))
                    .email(rs.getString("email"))
                    .password(rs.getString("password"))
                    .point(rs.getInt("point"))
                    .money(rs.getInt("money"))
                    .build()
    );

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public MemberEntity getMemberById(long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, memberEntityRowMapper, id);
    }

    public MemberEntity getMemberByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, memberEntityRowMapper, email);
    }

    public long addMember(Member member) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", member.getEmail());
        params.put("password", member.getPassword());
        params.put("point", member.getAvailablePointValue());
        params.put("money", member.getAvailableMoneyValue());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<MemberEntity> getAllMembers() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, memberEntityRowMapper);
    }

    public void updatePoint(long memberId, int updatePoint) {
        String sql = "UPDATE member SET point = ? WHERE id = ?";
        jdbcTemplate.update(sql, updatePoint, memberId);
    }

    public void updateMoney(long memberId, int updateMoney) {
        String sql = "UPDATE member SET money = ? WHERE id = ?";
        jdbcTemplate.update(sql, updateMoney, memberId);
    }
}
