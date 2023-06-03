package cart.service;

import cart.domain.member.Member;
import cart.repository.MemberRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        final Member member1 = memberRepository.save(new Member("pizza1@pizza.com", "password"));
        final Member member2 = memberRepository.save(new Member("pizza2@pizza.com", "password"));

        // when
        final List<Member> result = memberService.findAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(
                member1, member2
        ));
    }
}
