package cart.dao;

import static cart.fixture.DomainFixture.MEMBER_A;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class MemberDaoTest {

    MemberDao memberDao;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    @DisplayName("findById는 존재하는 id를 전달하면 해당 Member를 반환한다.")
    void findByIdSuccessTest() {
        Optional<Member> actual = memberDao.findById(MEMBER_A.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getId()).isEqualTo(MEMBER_A.getId())
        );
    }

    @Test
    @DisplayName("findByEmail은 존재하는 email을 전달하면 해당 Member를 반환한다.")
    void findByEmailSuccessTest() {
        String memberEmail = "a@a.com";
        Optional<Member> actual = memberDao.findByEmail(MEMBER_A.getEmail());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getEmail()).isEqualTo(MEMBER_A.getEmail())
        );
    }

    @Test
    @DisplayName("findAll은 존재하는 모든 Member를 반환한다.")
    void findAllSuccessTest() {
        List<Member> actual = memberDao.findAll();

        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("update는 변경할 정보를 가진 Member를 전달하면 해당 정보로 Member를 갱신한다.")
    void updateSuccessTest() {
        Member updateMember = new Member(MEMBER_A.getId(), "c@c.com", "abcd");

        memberDao.update(updateMember);

        Member actual = memberDao.findById(updateMember.getId()).get();

        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(updateMember.getId()),
                () -> assertThat(actual.getEmail()).isEqualTo(updateMember.getEmail()),
                () -> assertThat(actual.getPassword()).isEqualTo(updateMember.getPassword())
        );
    }
}
