package cart.dao.member;

import cart.domain.member.Member;
import cart.entity.member.MemberEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MemberEntity> rowMapper = (rs, rowNum) ->
            new MemberEntity(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password")
            );

    public MemberEntity getMemberById(final Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public MemberEntity getMemberByEmail(final String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, email);
    }

    // 추후 사용할 예정이라 남겨뒀습니다 :)
    public void addMember(final Member member) {
        String sql = "INSERT INTO member (email, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword());
    }

    public void updateMember(final Member member) {
        String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getId());
    }

    public void deleteMember(final Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<MemberEntity> getAllMemberEntities() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public boolean isExistMemberById(final Long memberId) {
        String sql = "SELECT COUNT(*) FROM member WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, memberId);
        return count > 0;
    }
}

