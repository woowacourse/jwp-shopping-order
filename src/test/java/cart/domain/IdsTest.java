package cart.domain;

import cart.exception.IdTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IdsTest {

    @Test
    @DisplayName("생성에 성공한다.")
    void construct_success() {
        // given
        final String input = "1,2,3,4,5";

        // when, then
        assertThatCode(() -> Ids.from(input))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("잘못된 구분자인 경우 생성에 실패한다.")
    void construct_fail_when_invalid_delimiter() {
        // given
        final String input = "1-2-3-4-5";

        // when, then
        assertThatThrownBy(() -> Ids.from(input))
                .isInstanceOf(IdTypeException.class);
    }

    @Test
    @DisplayName("잘못된 id 형식인 경우 생성에 실패한다.")
    void construct_fail_when_invalid_id_type() {
        // given
        final String input = "a,b,c,d,e";

        // when, then
        assertThatThrownBy(() -> Ids.from(input))
                .isInstanceOf(IdTypeException.class);
    }
}
