package cart.dao;

import static cart.fixture.MemberFixture.MEMBER_1;
import static cart.fixture.MemberFixture.MEMBER_2;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.domain.Member;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    @DisplayName("ID로 사용자를 조회한다.")
    void getMemberById() {
        // given
        Long id = memberDao.addMember(MEMBER_1);

        // when
        Member member = memberDao.getMemberById(id);

        // then
        assertThat(member)
                .extracting(Member::getId)
                .isEqualTo(id);
    }

    @Test
    @DisplayName("이메일로 사용자를 조회한다.")
    void getMemberByEmail() {
        // given
        Long id = memberDao.addMember(MEMBER_1);

        // when
        Member member = memberDao.getMemberByEmail(MEMBER_1.getEmail());

        // then
        assertThat(member)
                .extracting(Member::getId)
                .isEqualTo(id);
    }

    @Test
    @DisplayName("사용자를 추가한다.")
    void addMember() {
        // when
        Long id = memberDao.addMember(MEMBER_1);

        // then
        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("사용자 정보를 수정한다.")
    void updateMember() {
        // given
        Long id = memberDao.addMember(MEMBER_1);

        // when
        Member updateMember = new Member(id, MEMBER_2.getEmail(), MEMBER_1.getPassword());
        memberDao.updateMember(updateMember);

        // then
        Member member = memberDao.getMemberById(id);
        assertThat(member)
                .usingRecursiveComparison()
                .isEqualTo(updateMember);
    }

    @Test
    @DisplayName("사용자를 삭제한다.")
    void deleteMember() {
        // given
        Long id = memberDao.addMember(MEMBER_1);

        // when, then
        assertDoesNotThrow(
                () -> memberDao.deleteMember(id)
        );
    }

    @Test
    @DisplayName("전체 사용자를 조회한다.")
    void getAllMembers() {
        // given
        Long id1 = memberDao.addMember(MEMBER_1);
        Long id2 = memberDao.addMember(MEMBER_2);

        // when
        List<Member> members = memberDao.getAllMembers();

        // then
        assertThat(members)
                .extracting(Member::getId)
                .contains(id1, id2);
    }
}
