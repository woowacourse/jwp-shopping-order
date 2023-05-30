package cart.dao;

import java.util.List;

import cart.domain.member.Member;
import cart.exception.MemberNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Member> rowMapper = (rs, rowNum) -> {
        long memberId = rs.getLong("id");
        String email = rs.getString("email");
        String password = rs.getString("password");
        int cash = rs.getInt("cash");
        return new Member(memberId, email, password, cash);
    };

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member selectMemberById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream()
                .findAny()
                .orElseThrow(() -> new MemberNotFoundException("ID에 해당하는 멤버를 찾을 수 없습니다."));
    }

    public Member selectMemberByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        return jdbcTemplate.query(sql, rowMapper, email).stream()
                .findAny()
                .orElseThrow(() -> new MemberNotFoundException("이메일에 해당하는 멤버를 찾을 수 없습니다."));
    }

    public Boolean isNotExistByEmailAndPassword(String email, String password) {
        String sql = "SELECT EXISTS(SELECT 1 FROM member WHERE email = ? and password = ?)";
        return Boolean.FALSE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, email, password));
    }

    public void updateMemberCash(Member chargedMember) {
        String sql = "UPDATE member SET cash = ? WHERE id = ?";
        System.out.println("cash = " + chargedMember.getCash());
        jdbcTemplate.update(sql, chargedMember.getCash(), chargedMember.getId());
    }

    public List<Member> getAllMembers() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, rowMapper);
    }
}

