package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.domain.Member;

@JdbcTest
class MemberDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    MemberDao memberDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    @DisplayName("id를 통해 member를 조회한다.")
    void getMemberById() {
        //when
        final Member result = memberDao.getMemberById(1L);

        //then
        Assertions.assertAll(
                ()-> assertThat(result.getEmail()).isEqualTo("a@a.com"),
                ()-> assertThat(result.getPassword()).isEqualTo("1234"),
                ()-> assertThat(result.getGrade()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("email을 통해 member를 조회한다.")
    void getMemberByEmail() {
        final String email = "a@a.com";
        //when
        final Member result = memberDao.getMemberByEmail(email);

        //then
        Assertions.assertAll(
                ()-> assertThat(result.getEmail()).isEqualTo("a@a.com"),
                ()-> assertThat(result.getPassword()).isEqualTo("1234"),
                ()-> assertThat(result.getGrade()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("전체 멤버를 조회한다.")
    void getAllMembers() {
        //when
        final List<Member> result = memberDao.getAllMembers();

        //then
        assertThat(result.size()).isEqualTo(3);
    }
}
