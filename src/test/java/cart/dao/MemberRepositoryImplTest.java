package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class MemberRepositoryImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberRepository memberRepositoryImpl;

    @BeforeEach
    void setUp() {
        memberRepositoryImpl = new MemberRepositoryImpl(
                new MemberDao2(jdbcTemplate)
        );
    }

    @Test
    void 식별자로_사용자_조회_테스트() {
        final String memberEmail = "memberEmail";
        final Member member = new Member(memberEmail, "password");
        final Long saveMemberId = memberRepositoryImpl.saveMember(member);

        final Optional<Member> memberById = memberRepositoryImpl.findMemberById(saveMemberId);

        assertThat(memberById).isNotEmpty();
        assertThat(memberById.get().getEmail()).isEqualTo(memberEmail);
    }

    @Test
    void 이메일로_사용자_조회_테스트() {
        final String memberEmail = "memberEmail";
        final Member member = new Member(memberEmail, "password");
        final Long saveMemberId = memberRepositoryImpl.saveMember(member);

        final Optional<Member> memberById = memberRepositoryImpl.findMemberByEmail(memberEmail);

        assertThat(memberById).isNotEmpty();
        assertThat(memberById.get().getId()).isEqualTo(saveMemberId);
    }

    @Test
    void 조회_결과가_존재하지_않으면_null을_반환_한다() {
        final Optional<Member> memberByEmail = memberRepositoryImpl.findMemberByEmail("memberEmail");
        final Optional<Member> memberById = memberRepositoryImpl.findMemberById(1L);

        assertThat(memberByEmail).isEmpty();
        assertThat(memberById).isEmpty();
    }

    @Test
    void 사용자_전체_조회_테스트() {
        final Member memberA = new Member("memberEmailA", "password");
        final Member memberB = new Member("memberEmailB", "password");
        memberRepositoryImpl.saveMember(memberA);
        memberRepositoryImpl.saveMember(memberB);

        final List<Member> allMembers = memberRepositoryImpl.findAllMembers();

        assertThat(allMembers).hasSize(2);
    }

    @Test
    void 사용자_저장_테스트() {
        final Member member = new Member("memberEmailA", "password");

        memberRepositoryImpl.saveMember(member);

        final List<Member> allMembers = memberRepositoryImpl.findAllMembers();
        assertThat(allMembers).hasSize(1);
    }
}
