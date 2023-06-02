package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.support.MemberTestSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@Import({MemberTestSupport.class, MemberDao.class})
class MemberDaoTest {

    private MemberDao memberDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MemberTestSupport memberTestSupport;

    @BeforeEach
    void init() {
        memberDao = new MemberDao(jdbcTemplate);
    }

    @DisplayName("이메일로 회원을 조회한다.")
    @Test
    void getMemberByEmail() {
        //given
        Member member = memberTestSupport.builder().build();
        //when
        Member memberByEmail = memberDao.findById(member.getId());
        //then
        Assertions.assertAll(
                () -> assertThat(memberByEmail.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(memberByEmail.getPassword()).isEqualTo(member.getPassword())
        );
    }
}