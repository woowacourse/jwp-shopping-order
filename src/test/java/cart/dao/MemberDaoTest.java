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
    @DisplayName("findByEmailAndPassword 메서드는 ")
    class FindByEmailAndPassword {

        @Test
        @DisplayName("조회 시 이메일, 비밀번호와 일치하는 멤버가 존재하면 해당 멤버 데이터를 반환한다.")
        void findMember() {
            MemberEntity savedMember = memberDao.save(new MemberEntity("a@a.com", "password1", 0));

            Optional<MemberEntity> result = memberDao.findByEmailAndPassword(savedMember.getEmail(), savedMember.getPassword());

            assertAll(
                    () -> assertThat(result).isNotEmpty(),
                    () -> assertThat(result.get()).usingRecursiveComparison().isEqualTo(savedMember)
            );
        }

        @ParameterizedTest
        @CsvSource(value = {"b@b.com:password1", "a@a.com:password2", "b@b.com:password2"}, delimiter = ':')
        @DisplayName("조회 시 이메일, 비밀번호와 일치하는 멤버가 존재하지 않으면 빈 값을 반환한다.")
        void findEmpty(String email, String password) {
            memberDao.save(new MemberEntity("a@a.com", "password1", 0));

            Optional<MemberEntity> result = memberDao.findByEmailAndPassword(email, password);

            assertThat(result).isEmpty();
        }
    }
}
