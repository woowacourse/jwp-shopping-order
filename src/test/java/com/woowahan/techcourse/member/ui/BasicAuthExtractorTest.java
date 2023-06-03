package com.woowahan.techcourse.member.ui;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.woowahan.techcourse.cart.exception.AuthenticationException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
@DisplayNameGeneration(ReplaceUnderscores.class)
class BasicAuthExtractorTest {

    @Test
    void 헤더가_null일때_예외() {
        assertThatThrownBy(() -> BasicAuthExtractor.extractDecodedCredentials(null))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    void 헤더가_BASIC으로_시작하지_않으면_예외() {
        assertThatThrownBy(() -> BasicAuthExtractor.extractDecodedCredentials("header"))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    void 헤더에서_정상적으로_데이터를_가져올_수_있다() {
        AuthCredentials result = BasicAuthExtractor.extractDecodedCredentials("Basic dXNlcm5hbWU6cGFzc3dvcmQ=");

        assertSoftly(softly -> {
            softly.assertThat(result.getEmail()).isEqualTo("username");
            softly.assertThat(result.getPassword()).isEqualTo("password");
        });
    }
}
