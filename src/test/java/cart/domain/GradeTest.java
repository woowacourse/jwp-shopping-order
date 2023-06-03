package cart.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GradeTest {

    @Test
    void 존재하지_않는_이름의_등급으로_생성시_예외가_발생한다() {
        // given
        final String nonExistGrade = "ultraCapSsyong";

        // expect
        assertThatThrownBy(() -> Grade.from(nonExistGrade))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 이름의 등급은 존재하지 않습니다");
    }

}
