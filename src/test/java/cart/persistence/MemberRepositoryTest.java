package cart.persistence;

import cart.persistence.member.JdbcTemplateMemberDao;
import cart.domain.member.MemberRepository;
import cart.domain.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static cart.fixture.MemberFixture.하디;
import static cart.fixture.MemberFixture.현구막;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class MemberRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new JdbcTemplateMemberDao(jdbcTemplate);
    }

    @Test
    void 멤버를_추가하고_이메일로_조회한다() {
        // given, when
        memberRepository.addMember(하디);

        // then
        assertThat(memberRepository.findMemberByEmail(하디.getEmail()).isPresent())
                .isTrue();
    }

    @Test
    void 멤버를_이메일로_조회할_때_없으면_빈_옵셔널을_반환한다() {
        // given, when
        Optional<Member> member = memberRepository.findMemberByEmail("randomemail@random.com");

        // then
        assertThat(member.isEmpty()).isTrue();
    }

    @Test
    void 멤버를_모두_조회한다() {
        // given, when
        memberRepository.addMember(하디);
        memberRepository.addMember(현구막);

        // then
        assertThat(memberRepository.findAllMembers().size())
                .isEqualTo(2);
    }

    @Test
    void 전체멤버를_조회할_때_멤버가_없으면_빈_리스트를_반환한다() {
        // given, when
        List<Member> members = memberRepository.findAllMembers();

        // then
        assertThat(members).isEmpty();
    }

    @Test
    void 멤버를_업데이트한다() {
        // given
        memberRepository.addMember(하디);
        Member member = memberRepository.findMemberByEmail(하디.getEmail()).get();
        Member updateMember = new Member(member.getId(), 현구막.getEmail(), 현구막.getPassword());

        // when
        memberRepository.updateMember(updateMember);

        // then
        assertThat(memberRepository.findMemberById(member.getId()).get().getEmail())
                .isEqualTo(updateMember.getEmail());
    }

    @Test
    void 멤버를_삭제한다() {
        // given
        memberRepository.addMember(하디);
        Member member = memberRepository.findMemberByEmail(하디.getEmail()).get();

        // when
        memberRepository.deleteMember(member.getId());

        // then
        assertThat(memberRepository.findMemberByEmail(하디.getEmail()).isEmpty()).isTrue();
    }
}
