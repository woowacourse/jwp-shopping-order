package cart.repository.dao;

import cart.domain.member.Member;
import cart.repository.entity.MemberEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public Optional<MemberEntity> getMemberById(long id) {
        try {
            String sql = "SELECT * FROM member WHERE id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, memberEntityRowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<MemberEntity> getMemberByEmail(String email) {
        try {
            String sql = "SELECT * FROM member WHERE email = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, memberEntityRowMapper, email));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public long addMember(Member member) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", member.getEmail());
        params.put("password", member.getPassword());
        params.put("point", member.getAvailablePointValue());
        params.put("money", member.getAvailableMoneyValue());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public Optional<List<MemberEntity>> getAllMembers() {
        try {
            String sql = "SELECT * from member";
            return Optional.of(jdbcTemplate.query(sql, memberEntityRowMapper));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
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
