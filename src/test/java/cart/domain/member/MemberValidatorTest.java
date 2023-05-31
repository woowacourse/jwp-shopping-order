package cart.domain.member;

import cart.domain.fixture.Fixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberValidatorTest {

    @Test
    void 사용자가_일치하면_true_이다() {
        // given
        final MemberValidator memberValidator = new MemberValidator(Fixture.member1);

        // when
        final boolean isOwner = memberValidator.isOwner(Fixture.member1.getId());

        // then
        assertThat(isOwner).isTrue();
    }

    @Test
    void 사용자가_일치하지_않으면_false_이다() {
        // given
        final MemberValidator memberValidator = new MemberValidator(Fixture.member1);

        // when
        final boolean isOwner = memberValidator.isOwner(Fixture.member2.getId());

        // then
        assertThat(isOwner).isFalse();
    }
}