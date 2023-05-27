package cart.dao;

import static cart.fixtures.MemberFixtures.Dooly;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class MemberDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        this.memberDao = new MemberDao(jdbcTemplate);
    }

    @Nested
    @DisplayName("이메일과 비밀번호로 멤버 존재 여부 확인 시")
    class isNotExistByEmailAndPassword {

        @Test
        @DisplayName("멤버가 존재하지 않으면 TRUE를 반환한다.")
        void isNotExist_true() {
            // given
            String email = "notExist@email.com";
            String password = "notExistPassword";

            // when, then
            assertThat(memberDao.isNotExistByEmailAndPassword(email, password)).isTrue();
        }

        @Test
        @DisplayName("멤버가 존재하면 FALSE를 반환한다.")
        void isExist_false() {
            // given
            String email = Dooly.EMAIL;
            String password = Dooly.PASSWORD;

            // when, then
            assertThat(memberDao.isNotExistByEmailAndPassword(email, password)).isFalse();
        }
    }
}
