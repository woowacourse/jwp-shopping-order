package cart.controller;

import cart.exception.AuthenticationException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BasicTokenExtractorTest {

    @Test
    void 토큰의_시작이_Basic이_아니면_예외가_발생한다() {
        BasicTokenExtractor basicTokenExtractor = new BasicTokenExtractor();

        assertThatThrownBy(() -> basicTokenExtractor.decode("asic YUBhLmNvbToxMjM0"))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    void 복호화할_수_없는_토큰이면_예외가_발생한다() {
        BasicTokenExtractor basicTokenExtractor = new BasicTokenExtractor();

        assertThatThrownBy(() -> basicTokenExtractor.decode("Basic YUBhLmNvbBasic ToxMjM0"))
                .isInstanceOf(AuthenticationException.class);
    }
}
