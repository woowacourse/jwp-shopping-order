package cart.application;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.dto.PointResponse;
import cart.repository.MemberRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 멤버의_포인트를_조회한다() {
        // given
        Member 멤버 = new Member("이메일@aaa.com", "비밀번호", 1000);
        Long 저장된_멤버_ID = memberRepository.save(멤버);
        Member 저장된_멤버 = new Member(저장된_멤버_ID, 멤버.getEmail(), 멤버.getPassword(), 멤버.getPoint());

        // when
        PointResponse 응답 = memberService.getMemberPoint(저장된_멤버);

        // then
        assertThat(응답.getPoint()).isEqualTo(멤버.getPoint());
    }
}
