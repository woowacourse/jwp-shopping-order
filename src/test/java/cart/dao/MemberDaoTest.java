package cart.dao;

import cart.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static cart.fixture.Fixture.TEST_MEMBER;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class MemberDaoTest {
    private final MemberDao memberDao;
    @Autowired
    private MemberDaoTest(JdbcTemplate jdbcTemplate){
        memberDao = new MemberDao(jdbcTemplate);
    }
    @Test
    @DisplayName("id로 사용자를 찾는다.")
    void getMemberById() {
        Assertions.assertThat(memberDao.getMemberById(1L).getEmail()).isEqualTo(TEST_MEMBER.getEmail());
    }

    @Test
    @DisplayName("이메일로 사용자를 찾는다.")
    void getMemberByEmail() {
        Assertions.assertThat(memberDao.getMemberByEmail(TEST_MEMBER.getEmail()).getId()).isEqualTo(1L);
    }


    @Test
    void getAllMembers() {
        Assertions.assertThat(memberDao.getAllMembers()).hasSize(2);
    }
}
