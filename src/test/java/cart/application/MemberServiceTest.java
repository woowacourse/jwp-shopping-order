package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;

import cart.domain.Member;
import cart.repository.MemberRepository;
import cart.ui.controller.dto.response.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("findByEmailAndPassword 메서드는 이메일, 비밀번호를 통해 멤버를 응답한다.")
    void findByEmailAndPassword() {
        Member member = new Member(1L, "a@a.com", "password1", 0);
        given(memberRepository.findByEmailAndPassword(anyString(), anyString())).willReturn(member);

        MemberResponse response = memberService.findByEmailAndPassword("a@a.com", "password1");

        assertAll(
                () -> assertThat(response.getId()).isEqualTo(member.getId()),
                () -> assertThat(response.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(response.getPassword()).isEqualTo(member.getPassword()),
                () -> assertThat(response.getPoint()).isEqualTo(member.getPoint())
        );
    }
}
