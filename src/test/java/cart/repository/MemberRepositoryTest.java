package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.cart.Member;
import cart.test.RepositoryTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 사용자를_저장한다() {
        // given
        final Member member = new Member("pizza@pizza.com", "password");

        // when
        final Member savedMember = memberRepository.save(member);

        // then
        final List<Member> result = memberRepository.findAll();
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(savedMember).isEqualTo(result.get(0))
        );
    }

    @Test
    void 전체_사용자를_조회한다() {
        // given
        final Member member1 = memberRepository.save(new Member("pizza1@pizza.com", "password"));
        final Member member2 = memberRepository.save(new Member("pizza2@pizza.com", "password"));

        // when
        List<Member> result = memberRepository.findAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(member1, member2));
    }

    @Test
    void 단일_사용자를_조회한다() {
        // given
        final Member member = memberRepository.save(new Member("pizza1@pizza.com", "password"));

        // when
        final Optional<Member> result = memberRepository.findById(member.getId());

        // then
        assertThat(result).isPresent();
    }
}
