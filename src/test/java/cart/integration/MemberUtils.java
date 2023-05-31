package cart.integration;

import cart.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;

public class MemberUtils {

    public static void 회원_추가(Member member, JdbcTemplate jdbcTemplate) {
        String memberSql = "INSERT INTO member (id, email, password) VALUES (?, ?,?)";
        jdbcTemplate.update(memberSql, member.getId(), member.getEmail(), member.getPassword());
    }
}
