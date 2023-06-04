package cart.member.dao;

import cart.init.DBInit;
import cart.member.domain.Member;
import cart.member.repository.MemberEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class MemberDaoTest extends DBInit {
    public static final MemberEntity A_MEMBER_ENTITY = new MemberEntity(1L, "a@a.com", "1234", 30000L);
    public static final MemberEntity C_MEMBER_ENTITY = new MemberEntity(3L, "c@c.com", "1234", 1000L);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void memberId로_회원을_조회한다() {
        // given
        final long memberId = 1L;

        // when
        final MemberEntity memberEntity = memberDao.getMemberById(memberId);

        // then
        assertThat(memberEntity).isEqualTo(A_MEMBER_ENTITY);
    }

    @Test
    void memberEmail로_회원을_조회한다() {
        // given
        final String email = "a@a.com";

        // when
        final MemberEntity memberEntity = memberDao.getMemberByEmail(email);

        // then
        assertThat(memberEntity).isEqualTo(A_MEMBER_ENTITY);
    }

    @Test
    void 회원을_추가한다() {
        // given
        final long memberId = 3L;

        // when
        memberDao.addMember(C_MEMBER_ENTITY);

        // then
        assertThat(memberDao.getMemberById(memberId)).isEqualTo(new MemberEntity(memberId, "c@c.com", "1234", 1000L));
    }

    @Test
    void 회원_정보를_수정한다() {
        // given
        final long memberId = 2L;
        final String email = "b@b.com";
        final String password = "5678";
        final long point = 300000L;
        final MemberEntity updatedMemberEntity = new MemberEntity(memberId, email, password, point);

        // when
        memberDao.update(updatedMemberEntity);

        // then
        assertAll(
                () -> assertThat(updatedMemberEntity.getId()).isEqualTo(memberId),
                () -> assertThat(updatedMemberEntity.getEmail()).isEqualTo(email),
                () -> assertThat(updatedMemberEntity.getPassword()).isEqualTo(password),
                () -> assertThat(updatedMemberEntity.getPoint()).isEqualTo(point)
        );
    }

    @Test
    void memberId로_회원을_삭제한다() {
        // given
        final long memberId = 1L;

        // when
        memberDao.deleteMember(memberId);

        // then
        assertThat(memberDao.getAllMembers()).hasSize(1);
    }

    @Test
    void 모든_회원을_조회한다() {
        // when
        final List<Member> allMembers = memberDao.getAllMembers();

        // then
        assertThat(allMembers).hasSize(2);
    }
}
