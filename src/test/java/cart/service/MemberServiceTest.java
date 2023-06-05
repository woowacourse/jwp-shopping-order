package cart.service;

import static cart.fixture.MemberFixture.사용자1;
import static cart.fixture.MemberFixture.사용자2;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.member.Member;
import cart.dto.member.MemberResponse;
import cart.repository.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 모든_사용자를_조회한다() {
        // given
        final Member member1 = memberRepository.save(사용자1);
        final Member member2 = memberRepository.save(사용자2);

        // when
        final List<MemberResponse> result = memberService.findAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(
                new MemberResponse(member1.getId(), member1.getEmail(), member1.getPassword()),
                new MemberResponse(member2.getId(), member2.getEmail(), member2.getPassword())
        ));
    }
}
