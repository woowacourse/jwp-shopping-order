package cart.application.service;

import cart.application.exception.MemberNotFoundException;
import cart.application.repository.MemberRepository;
import cart.application.domain.Member;
import cart.application.exception.AuthenticationException;
import cart.application.service.MemberService;
import cart.presentation.dto.request.AuthInfo;
import cart.presentation.dto.response.PointResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setup() {
        member = new Member(1L, "teo", "1234", 1000);
    }

    @Test
    @DisplayName("해당 정보의 멤버가 없다면 예외를 던진다")
    void getPoint_exception() {
        // given
        AuthInfo authInfo = new AuthInfo("teo", "1234");
        when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());
        // when, then
        assertThatThrownBy(() -> memberService.getPoint(authInfo))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("해당 정보의 멤버를 찾아 포인트를 반환한다")
    void getPoint() {
        // given
        AuthInfo authInfo = new AuthInfo("teo", "1234");
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        // when
        PointResponse pointResponse = memberService.getPoint(authInfo);
        // then
        assertThat(pointResponse.getPoint()).isEqualTo(1000);
    }

    @Test
    @DisplayName("해당 정보의 멤버가 없다면 예외를 던진다")
    void validateMemberProfile_exception() {
        // given
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));

        // when, then
        assertThatThrownBy(() -> memberService.validateMemberProfile("teo", "1233"))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("해당 정보의 멤버가 있다면 예외를 던지지 않는다")
    void validateMemberProfile() {
        // given
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));

        // when, then
        assertThatCode(() -> memberService.validateMemberProfile("teo", "1234"))
                .doesNotThrowAnyException();
    }
}
