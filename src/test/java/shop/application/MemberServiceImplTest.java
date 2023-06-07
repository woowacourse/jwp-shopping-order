package shop.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import shop.application.member.MemberService;
import shop.application.member.dto.MemberDto;
import shop.application.member.dto.MemberJoinDto;
import shop.application.member.dto.MemberLoginDto;
import shop.domain.member.Member;
import shop.domain.repository.MemberRepository;
import shop.util.Encryptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberServiceImplTest extends ServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("회원 가입을 할 수 있다")
    @Test
    void joinTest() {
        //given
        MemberJoinDto memberJoinDto = new MemberJoinDto("쥬니쥬니", "바다바다");

        //when
        memberService.join(memberJoinDto);

        //then
        Member findMember = memberRepository.findByName("쥬니쥬니");
        assertThat(findMember.getPassword())
                .isEqualTo(Encryptor.encrypt(memberJoinDto.getPassword()));
    }

    @DisplayName("로그인을 할 수 있다")
    @Test
    void login() {
        //given
        MemberJoinDto memberJoinDto = new MemberJoinDto("쥬니쥬니", "바다바다");
        MemberLoginDto memberLoginDto = new MemberLoginDto("쥬니쥬니", "바다바다");

        //when
        memberService.join(memberJoinDto);

        //then
        String token = memberService.login(memberLoginDto);
        String decodedToken = new String(Base64Utils.decodeFromUrlSafeString(token));
        String expectedDecodedToken = "쥬니쥬니:" + Encryptor.encrypt("바다바다");

        assertThat(decodedToken).isEqualTo(expectedDecodedToken);
    }

    @DisplayName("모든 회원을 조회할 수 있다.")
    @Test
    void getAllMembersTest() {
        List<MemberDto> allMembers = memberService.getAllMembers();

        assertThat(allMembers.size()).isEqualTo(2); //initialization.sql
        assertThat(allMembers).extractingResultOf("getName")
                .containsExactlyInAnyOrder("a@a.com", "b@b.com");
    }
}
