package cart.authentication;

import static cart.fixture.DomainFixture.MEMBER_A;
import static cart.fixture.DomainFixture.MEMBER_B;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import cart.domain.Member;
import cart.exception.AuthenticationException;
import cart.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthenticationMemberConverterTest {

    MemberRepository memberRepository;
    AuthenticationMemberConverter memberConverter;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        memberConverter = new AuthenticationMemberConverter(memberRepository);
    }

    @Test
    @DisplayName("convert는 AuthInfo를 전달하면 해당 정보에 맞는 Member를 반환한다.")
    void convertSuccessTest() {
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(MEMBER_A));
        AuthInfo authInfo = new AuthInfo(MEMBER_A.getEmail(), MEMBER_A.getPassword());

        Member actual = memberConverter.convert(authInfo);

        assertThat(actual.getId()).isEqualTo(MEMBER_A.getId());
    }

    @Test
    @DisplayName("convert는 존재하지 않는 이메일의 AuthInfo를 전달하면 예외가 발생한다.")
    void convertFailTestWithNotExistEmail() {
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.empty());
        AuthInfo authInfo = new AuthInfo(MEMBER_A.getEmail(), MEMBER_A.getPassword());

        assertThatThrownBy(() -> memberConverter.convert(authInfo))
                .isInstanceOf(AuthenticationException.InvalidMember.class)
                .hasMessage("유효하지 않은 회원입니다.");
    }

    @Test
    @DisplayName("convert는 비밀번호가 일치하지 않는 AuthInfo를 전달하면 예외가 발생한다.")
    void convertFailWithNotMatchPassword() {
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(MEMBER_B));
        AuthInfo authInfo = new AuthInfo(MEMBER_A.getEmail(), "1234567");

        assertThatThrownBy(() -> memberConverter.convert(authInfo))
                .isInstanceOf(AuthenticationException.InvalidMember.class)
                .hasMessage("유효하지 않은 회원입니다.");
    }
}
