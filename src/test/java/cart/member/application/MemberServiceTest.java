package cart.member.application;

import cart.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static cart.member.domain.MemberTest.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void 해당_회원의_현재_적립금을_확인한다() {
        // given
        given(memberRepository.getMemberByEmail("a@a.com")).willReturn(MEMBER);

        // when
        final Long currentPoint = memberService.getCurrentPoint(MEMBER);

        // then
        assertThat(currentPoint).isEqualTo(30000L);
    }
}
