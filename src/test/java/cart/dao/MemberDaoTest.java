package cart.dao;

import cart.domain.Member;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.fixtures.MemberFixtures.MemberA;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SuppressWarnings("NonAsciiCharacters")
class MemberDaoTest extends DaoTest {

    @Test
    void 멤버_id를_통해_멤버를_찾는다() {
        final Member member = memberDao.getMemberById(1L);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(member.getId()).isEqualTo(1L);
            softAssertions.assertThat(member.getEmail()).isEqualTo("a@a.com");
            softAssertions.assertThat(member.getPassword()).isEqualTo("1234");
        });
    }

    @Test
    void 이메일_주소를_통해_멤버를_찾는다() {
        final Member member = memberDao.getMemberByEmail("a@a.com");

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(member.getId()).isEqualTo(1L);
            softAssertions.assertThat(member.getEmail()).isEqualTo("a@a.com");
            softAssertions.assertThat(member.getPassword()).isEqualTo("1234");
        });
    }

    @Test
    void 멤버를_추가하다() {
        final Member member = Member.of(null, "test@test.com", "1234", 0L);

        assertDoesNotThrow(() -> memberDao.addMember(member));
    }

    @Test
    void 멤버_정보를_변경하다() {
        final Member member = Member.of(1L, "test@test.com", "1234", 0L);

        assertDoesNotThrow(() -> memberDao.updateMember(member));
    }

    @Test
    void 멤버를_삭제하다() {
        assertDoesNotThrow(() -> memberDao.deleteMember(1L));
    }

    @Test
    void 모든_멤버들을_구한다() {
        final List<Member> members = memberDao.getAllMembers();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(members).hasSize(4);
            softAssertions.assertThat(members.get(0)).isEqualTo(MemberA.ENTITY);
        });
    }
}
