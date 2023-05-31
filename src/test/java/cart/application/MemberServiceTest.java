package cart.application;

import cart.application.repository.MemberRepository;
import cart.application.domain.Member;
import cart.application.exception.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setup() {
        member = new Member(1L, "teo", "1234", 0);
    }

    @Test
    void 해당_정보의_멤버가_없다면_예외를_던진다() {
        // given
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));

        // when, then
        assertThatThrownBy(() -> memberService.validateMemberProfile("teo", "1233"))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    void 해당_정보의_멤버가_있다면_예외를_던지지_않는다() {
        // given
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));

        // when, then
        assertThatCode(() -> memberService.validateMemberProfile("teo", "1234"))
                .doesNotThrowAnyException();
    }
}
