package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
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
    void 사용자_저장_테스트() {
        final String memberEmail = "email";
        final Member memberEntity = new Member(memberEmail, "password");

        memberDao.saveMember(memberEntity);

        final List<Member> allMembers = memberDao.findAllMembers();
        assertThat(allMembers).hasSize(1);
        assertThat(allMembers.get(0).getEmail()).isEqualTo(memberEmail);
        assertThat(allMembers.get(0).getId()).isNotNull();
    }

    @Test
    void 식별자로_사용자_조회_테스트() {
        final String memberEmail = "memberEmail";
        final Member memberEntity = new Member(memberEmail, "password");
        final Long saveMemberId = memberDao.saveMember(memberEntity);

        final Optional<Member> member = memberDao.findMemberById(saveMemberId);

        assertThat(member).isNotEmpty();
        assertThat(member.get().getEmail()).isEqualTo(memberEmail);
    }

    @Test
    void 이메일로_사용자_조회_테스트() {
        final String memberEmail = "memberEmail";
        final Member memberEntity = new Member(memberEmail, "password");
        final Long saveMemberId = memberDao.saveMember(memberEntity);

        final Optional<Member> member = memberDao.findMemberByEmail(memberEmail);

        assertThat(member).isNotEmpty();
        assertThat(member.get().getId()).isEqualTo(saveMemberId);
    }

    @Test
    void 존재하지_않으면_null을_반환_한다() {
        final Optional<Member> member = memberDao.findMemberByEmail("memberEmail");

        assertThat(member).isEmpty();
    }

    @Test
    void 사용자_전체_조회_테스트() {
        final Member memberEntityA = new Member("memberEmailA", "password");
        final Member memberEntityB = new Member("memberEmailB", "password");
        memberDao.saveMember(memberEntityA);
        memberDao.saveMember(memberEntityB);

        final List<Member> allMembers = memberDao.findAllMembers();

        assertThat(allMembers).hasSize(2);
    }
}
