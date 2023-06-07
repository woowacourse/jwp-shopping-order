package cart.presentation;

import cart.exception.application.IllegalMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ExceptionStatusMapperTest {

    @Test
    @DisplayName("해당하는 예외에 맞는 상태코드를 반환할 수 있다")
    void exceptionStatusMapping() {
        // given
        IllegalMemberException exception = new IllegalMemberException();
        // when
        HttpStatus status = ExceptionStatusMapper.of(exception);
        // then
        assertThat(status).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("해당하는 예외가 없다면 예외를 던진다")
    void exceptionStatusMapping_exception() {
        // given
        Exception exception = new Exception();
        // when, then
        assertThatThrownBy(() -> ExceptionStatusMapper.of(exception))
                .isInstanceOf(IllegalStateException.class);
    }
}
