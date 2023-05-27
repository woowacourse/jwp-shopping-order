package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;

import cart.domain.Member;
import cart.repository.MemberRepository;
import cart.ui.controller.dto.response.MemberResponse;
import java.util.List;
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
    @DisplayName("getAllMembers 메서드는 모든 멤버를 응답한다.")
    void getAllMembers() {
        Member memberA = new Member(1L, "a@a.com", "password1", 0);
        Member memberB = new Member(2L, "b@b.com", "password2", 0);
        given(memberRepository.getAllMembers()).willReturn(List.of(memberA, memberB));

        List<MemberResponse> response = memberService.getAllMembers();

        assertAll(
                () -> assertThat(response).hasSize(2),
                () -> assertThat(response.get(0)).usingRecursiveComparison().isEqualTo(MemberResponse.from(memberA)),
                () -> assertThat(response.get(1)).usingRecursiveComparison().isEqualTo(MemberResponse.from(memberB))
        );
    }

    @Test
    @DisplayName("getMemberByEmailAndPassword 메서드는 이메일, 비밀번호를 통해 멤버를 응답한다.")
    void GetMemberByEmailAndPassword() {
        Member member = new Member(1L, "a@a.com", "password1", 0);
        given(memberRepository.getMemberByEmailAndPassword(anyString(), anyString())).willReturn(member);

        MemberResponse response = memberService.getMemberByEmailAndPassword("a@a.com", "password1");

        assertAll(
                () -> assertThat(response.getId()).isEqualTo(member.getId()),
                () -> assertThat(response.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(response.getPassword()).isEqualTo(member.getPassword()),
                () -> assertThat(response.getPoint()).isEqualTo(member.getPoint())
        );
    }
}
