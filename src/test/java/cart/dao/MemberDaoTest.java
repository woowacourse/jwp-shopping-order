package cart.dao;

import static cart.fixtures.MemberFixtures.Dooly;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.member.Member;
import cart.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@ActiveProfiles("test")
class MemberDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;

    @BeforeEach
    @Sql({"/truncate.sql", "/data.sql"})
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

    @Nested
    @DisplayName("이메일로 멤버 조회 시")
    class selectMemberByEmail {

        @Test
        @DisplayName("멤버가 존재하면 멤버를 반환한다.")
        void isExist() {
            // given
            String email = Dooly.EMAIL;

            // when
            Member findMember = memberDao.selectMemberByEmail(email);

            // then
            assertThat(findMember).usingRecursiveComparison().isEqualTo(Dooly.ENTITY());
        }

        @Test
        @DisplayName("멤버가 존재하지 않으면 예외가 발생한다.")
        void throws_when_not_exist_member() {
            // given
            String email = "notExist@email.com";

            // when, then
            assertThatThrownBy(() -> memberDao.selectMemberByEmail(email))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage("이메일에 해당하는 멤버를 찾을 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("ID로 멤버 조회 시")
    class selectMemberById {

        @Test
        @DisplayName("멤버가 존재하면 멤버를 반환한다.")
        void isExist() {
            // given
            Long existId = Dooly.ID;

            // when
            Member findMember = memberDao.selectMemberById(existId);

            // then
            assertThat(findMember).usingRecursiveComparison().isEqualTo(Dooly.ENTITY());
        }

        @Test
        @DisplayName("멤버가 존재하지 않으면 예외가 발생한다.")
        void throws_when_not_exist_member() {
            // given
            Long notExistId = -1L;

            // when, then
            assertThatThrownBy(() -> memberDao.selectMemberById(notExistId))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage("ID에 해당하는 멤버를 찾을 수 없습니다.");
        }
    }

    @Test
    @DisplayName("해당하는 멤버의 금액을 업데이트한다.")
    void updateMemberCash() {
        // given
        Member member = memberDao.selectMemberById(Dooly.ID);
        Member chargedMember = member.chargeCash(10000);

        // when
        memberDao.updateMemberCash(chargedMember);
        Member memberAfterCharge = memberDao.selectMemberById(Dooly.ID);

        // then
        assertThat(memberAfterCharge.getCash()).isEqualTo(15000);
    }
}
