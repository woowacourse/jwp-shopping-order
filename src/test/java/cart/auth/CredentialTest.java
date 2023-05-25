package cart.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class CredentialTest {

    @CsvSource(value = {"pizza, false", "pizz4, true"})
    @ParameterizedTest(name = "비밀번호가 동일하지 않은지 확인한다. PW: pizza, 비교대상: {0}, 결과 {1}")
    void 비밀번호가_다른지_확인한다(final String password, final boolean result) {
        // given
        final Credential credential = new Credential("pizza@pizza.com", "pizza");
        final Credential findCredential = new Credential("pizza@pizza.com", password);

        // expect
        assertThat(credential.isNotSamePassword(findCredential)).isEqualTo(result);
    }
}
