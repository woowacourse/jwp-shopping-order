package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.DaoTest;
import cart.dao.entity.MemberEntity;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MemberDaoTest extends DaoTest {

    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Nested
    @DisplayName("getMemberById 메서드는 ")
    class GetMemberById {

        @Test
        @DisplayName("조회 시 ID와 일치하는 멤버가 존재하면 해당 멤버 데이터를 반환한다.")
        void getMember() {
            String email = "a@a.com";
            String password = "password1";
            Long savedMemberId = memberDao.addMember(new MemberEntity(email, password, 0));

            Optional<MemberEntity> result = memberDao.getMemberById(savedMemberId);

            MemberEntity findMember = memberDao.getMemberByEmailAndPassword(email, password).get();
            assertAll(
                    () -> assertThat(result).isNotEmpty(),
                    () -> assertThat(result.get()).usingRecursiveComparison().isEqualTo(findMember)
            );
        }

        @Test
        @DisplayName("조회 시 ID와 일치하는 멤버가 존재하지 않으면 빈 값을 반환한다.")
        void getEmpty() {
            Optional<MemberEntity> result = memberDao.getMemberById(-1L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getMemberByEmailAndPassword 메서드는 ")
    class GetMemberByEmailAndPassword {

        @Test
        @DisplayName("조회 시 이메일, 비밀번호와 일치하는 멤버가 존재하면 해당 멤버 데이터를 반환한다.")
        void getMember() {
            String email = "a@a.com";
            String password = "password1";
            Long savedMemberId = memberDao.addMember(new MemberEntity(email, password, 0));

            Optional<MemberEntity> result = memberDao.getMemberByEmailAndPassword(email, password);

            MemberEntity findMember = memberDao.getMemberById(savedMemberId).get();
            assertAll(
                    () -> assertThat(result).isNotEmpty(),
                    () -> assertThat(result.get()).usingRecursiveComparison().isEqualTo(findMember)
            );
        }

        @ParameterizedTest
        @CsvSource(value = {"b@b.com:password1", "a@a.com:password2", "b@b.com:password2"}, delimiter = ':')
        @DisplayName("조회 시 이메일, 비밀번호와 일치하는 멤버가 존재하지 않으면 빈 값을 반환한다.")
        void getEmpty(String email, String password) {
            memberDao.addMember(new MemberEntity("a@a.com", "password1", 0));

            Optional<MemberEntity> result = memberDao.getMemberByEmailAndPassword(email, password);

            assertThat(result).isEmpty();
        }
    }
}
