package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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

    private MemberDao2 memberDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao2(jdbcTemplate);
    }

    @Test
    void 사용자_저장_테스트() {
        final String memberEmail = "email";
        final MemberEntity memberEntity = new MemberEntity(memberEmail, "password");

        memberDao.saveMember(memberEntity);

        final List<MemberEntity> allMembers = memberDao.findAllMembers();
        assertThat(allMembers).hasSize(1);
        assertThat(allMembers.get(0).getEmail()).isEqualTo(memberEmail);
        assertThat(allMembers.get(0).getId()).isNotNull();
    }

    @Test
    void 식별자로_사용자_조회_테스트() {
        final String memberEmail = "memberEmail";
        final MemberEntity memberEntity = new MemberEntity(memberEmail, "password");
        final Long saveMemberId = memberDao.saveMember(memberEntity);

        final MemberEntity member = memberDao.findMemberById(saveMemberId);

        assertThat(member.getEmail()).isEqualTo(memberEmail);
    }

    @Test
    void 이메일로_사용자_조회_테스트() {
        final String memberEmail = "memberEmail";
        final MemberEntity memberEntity = new MemberEntity(memberEmail, "password");
        final Long saveMemberId = memberDao.saveMember(memberEntity);

        final MemberEntity member = memberDao.findMemberByEmail(memberEmail);

        assertThat(member.getId()).isEqualTo(saveMemberId);
    }

    @Test
    void 존재하지_않으면_null을_반환_한다() {
        final MemberEntity member = memberDao.findMemberByEmail("memberEmail");

        assertThat(member).isNull();
    }

    @Test
    void 사용자_전체_조회_테스트() {
        final MemberEntity memberEntityA = new MemberEntity("memberEmailA", "password");
        final MemberEntity memberEntityB = new MemberEntity("memberEmailB", "password");
        memberDao.saveMember(memberEntityA);
        memberDao.saveMember(memberEntityB);

        final List<MemberEntity> allMembers = memberDao.findAllMembers();

        assertThat(allMembers).hasSize(2);
    }
}
