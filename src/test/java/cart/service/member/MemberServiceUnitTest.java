package cart.service.member;

import cart.domain.member.Member;
import cart.dto.member.MemberCreateRequest;
import cart.dto.member.MemberResponse;
import cart.exception.MemberAlreadyExistException;
import cart.repository.cart.CartRepository;
import cart.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceUnitTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CartRepository cartRepository;

    @DisplayName("멤버를 저장한다.")
    @Test
    void create_member() {
        // given
        MemberCreateRequest req = new MemberCreateRequest("a@a.com", "1234");

        given(memberRepository.isExistMemberByEmail(req.getEmail())).willReturn(false);

        // when
        memberService.createMember(req);

        // then
        verify(memberRepository).save(any(Member.class));
        verify(cartRepository).createMemberCart(anyLong());
    }

    @DisplayName("이미 존재하는 이메일이면 예외를 발생시킨다.")
    @Test
    void throws_exception_when_already_being_email() {
        // given
        MemberCreateRequest req = new MemberCreateRequest("a@a.com", "1234");
        given(memberRepository.isExistMemberByEmail(req.getEmail())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.createMember(req))
                .isInstanceOf(MemberAlreadyExistException.class);
    }

    @DisplayName("멤버를 전체 조회한다.")
    @Test
    void find_all_members() {
        // given
        List<MemberResponse> expected = List.of(MemberResponse.from(new Member(1L, "a@a.com", "1234")));
        List<Member> members = List.of(new Member(1L, "a@a.com", "1234"));
        given(memberRepository.findAll()).willReturn(members);

        // when
        List<MemberResponse> result = memberService.findAll();

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(1),
                () -> assertThat(result.get(0).getEmail()).isEqualTo(expected.get(0).getEmail())
        );
    }

    @DisplayName("멤버를 단건 조회한다.")
    @Test
    void find_member() {
        // given
        long memberId = 1;
        Member member = new Member(1L, "a@a.com", "1234");
        given(memberRepository.findMemberById(memberId)).willReturn(member);

        // when
        MemberResponse result = memberService.findById(memberId);

        // then
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
    }

    @DisplayName("멤버를 삭제한다.")
    @Test
    void delete_member() {
        // when
        memberService.deleteById(1L);

        // then
        verify(memberRepository).deleteById(any());
    }
}
