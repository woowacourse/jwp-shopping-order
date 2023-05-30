package cart.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static cart.fixtures.MemberFixtures.MemberA;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTest {

    @Nested
    class checkPassword_테스트 {

        @Test
        void 비밀번호가_같으면_True를_반환하는지_확인하다() {
            assertThat(MemberA.ENTITY.checkPassword("1234")).isTrue();
        }

        @Test
        void 비밀번호가_다르면_False를_반환하는지_확인하다() {
            assertThat(MemberA.ENTITY.checkPassword("4321")).isFalse();
        }
    }
}
