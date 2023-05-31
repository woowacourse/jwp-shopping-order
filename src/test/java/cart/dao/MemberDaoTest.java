package cart.dao;

import cart.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ActiveProfiles("test")
@JdbcTest
class MemberDaoTest {

    private static final Member ADDED_MEMBER = new Member("sunshot@woowacourse.com", "sunshot1!", 0L);

    private MemberDao memberDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        memberDao = new MemberDao(jdbcTemplate);
    }

    @DisplayName("getMemberById 테스트")
    @Test
    void getMemberByIdTest() {
        long memberId = memberDao.addMember(ADDED_MEMBER);
        Member member = memberDao.getMemberById(memberId);

        assertSoftly(softly -> {
            softly.assertThat(member.getId()).isEqualTo(memberId);
            softly.assertThat(member.getEmail()).isEqualTo(ADDED_MEMBER.getEmail());
            softly.assertThat(member.getPassword()).isEqualTo(ADDED_MEMBER.getPassword());
            softly.assertThat(member.getPoint()).isEqualTo(ADDED_MEMBER.getPoint());
        });
    }

    @DisplayName("getMemberByEmail 태스트")
    @Test
    void getMemberByEmailTest() {
        memberDao.addMember(ADDED_MEMBER);
        Member member = memberDao.getMemberByEmail(ADDED_MEMBER.getEmail());

        assertSoftly(softly -> {
            softly.assertThat(member.getEmail()).isEqualTo(ADDED_MEMBER.getEmail());
            softly.assertThat(member.getPassword()).isEqualTo(ADDED_MEMBER.getPassword());
            softly.assertThat(member.getPoint()).isEqualTo(ADDED_MEMBER.getPoint());
        });
    }

    @DisplayName("updateMember 테스트")
    @Test
    void updateMemberTest() {
        Long memberId = memberDao.addMember(ADDED_MEMBER);
        Member beforeMember = memberDao.getMemberById(memberId);

        memberDao.updateMember(new Member(memberId, "sunshot@woowacourse.com", "sunshot2@", 1000L));
        Member afterMember = memberDao.getMemberById(memberId);

        assertSoftly(softly -> {
            softly.assertThat(beforeMember.getId()).isEqualTo(memberId);
            softly.assertThat(afterMember.getId()).isEqualTo(memberId);
            softly.assertThat(beforeMember.getEmail()).isEqualTo(ADDED_MEMBER.getEmail());
            softly.assertThat(afterMember.getEmail()).isEqualTo("sunshot@woowacourse.com");
            softly.assertThat(beforeMember.getPassword()).isEqualTo(ADDED_MEMBER.getPassword());
            softly.assertThat(afterMember.getPassword()).isEqualTo("sunshot2@");
            softly.assertThat(beforeMember.getPoint()).isEqualTo(ADDED_MEMBER.getPoint());
            softly.assertThat(afterMember.getPoint()).isEqualTo(1000);
        });
    }

    @DisplayName("deleteMember 테스트")
    @Test
    void deleteMemberTest() {
        Long memberId = memberDao.addMember(ADDED_MEMBER);
        int affectedRow = memberDao.deleteMember(memberId);

        assertSoftly(softly -> {
            softly.assertThat(affectedRow).isEqualTo(1);
            softly.assertThatThrownBy(() -> memberDao.getMemberById(memberId))
                    .isInstanceOf(DataAccessException.class);
        });
    }

    @DisplayName("getAllMembers 테스트")
    @Test
    void getAllMembers() {
        // data.sql 파일에서 2명의 member를 사전에 추가
        List<Member> members = memberDao.getAllMembers();
        assertThat(members).hasSize(2);
    }
}
