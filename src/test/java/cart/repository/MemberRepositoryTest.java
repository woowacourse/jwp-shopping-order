package cart.repository;

import cart.domain.Member;
import cart.test.RepositoryTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        final Member member1 = new Member("pizza1@pizza.com", "password");
        final Member member2 = new Member("pizza2@pizza.com", "password");
        final Member savedMember1 = memberRepository.save(member1);
        final Member savedMember2 = memberRepository.save(member2);

        // when
        List<Member> result = memberRepository.findAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(savedMember1, savedMember2));
    }

    @Test
    void 단일_사용자를_조회한다() {
        // given
        final Member member = new Member("pizza1@pizza.com", "password");
        final Member savedMember = memberRepository.save(member);

        // when
        final Optional<Member> result = memberRepository.findById(savedMember.getId());

        // then
        assertThat(result).isPresent();
    }
}
