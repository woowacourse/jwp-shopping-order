package cart.repository;

import cart.dao.MemberDao;
import cart.dao.MemberEntity;
import cart.domain.member.Email;
import cart.domain.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Sql(value = "classpath:test_truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class MemberRepositoryTest {

    private static final MemberEntity MEMBER_ENTITY = new MemberEntity("huchu@woowahan.com", "1234567a!", 0);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        MemberDao memberDao = new MemberDao(jdbcTemplate);
        memberRepository = new MemberRepository(memberDao);

        memberDao.addMember(MEMBER_ENTITY);
    }

    @Test
    void 모든_회원을_반환한다() {
        //when
        final List<Member> allMembers = memberRepository.getAllMembers();

        //then
        assertThat(allMembers).usingRecursiveComparison()
                .isEqualTo(List.of(new Member(1L, "huchu@woowahan.com", "1234567a!", 0)));
    }

    @Test
    void 이메일로_회원을_찾는다() {
        //given
        final Email email = new Email("huchu@woowahan.com");

        //when
        final Member member = memberRepository.getMemberByEmail(email);

        //then
        assertThat(member).isEqualTo(new Member(1L, "huchu@woowahan.com", "1234567a!", 0));
    }
}
