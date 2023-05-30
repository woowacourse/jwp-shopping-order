package cart.service;

import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.repository.MemberRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    void 모든_회원_정보를_얻는다() {
        //given
        Mockito.when(memberRepository.getAllMembers())
                .thenReturn(List.of(new Member(1L, "huchu@woowahan.com", "1234567a!")));

        //when
        final List<Member> allMembers = memberService.getAllMembers();

        //then
        assertThat(allMembers).isEqualTo(List.of(new Member(1L, "huchu@woowahan.com", "1234567a!")));
    }

    @Test
    void 이메일로_회원을_얻는다() {
        //given
        final String email = "huchu@woowahan.com";

        Mockito.when(memberRepository.getMemberByEmail(any(Email.class)))
                .thenReturn(new Member(1L, email, "1234567a!"));

        //when
        final Member member = memberService.getMemberByEmail(new Email(email));

        //then
        assertThat(member).isEqualTo(new Member(1L, "huchu@woowahan.com", "1234567a!"));
    }
}
