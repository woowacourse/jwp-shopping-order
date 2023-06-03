package cart.presentation;

import cart.application.exception.IllegalMemberException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ExceptionStatusMapperTest {

    @Test
    void 해당하는_예외에_맞는_상태코드를_반환할_수_있다() {
        // given
        IllegalMemberException exception = new IllegalMemberException();

        // when
        HttpStatus status = ExceptionStatusMapper.of(exception);

        // then
        assertThat(status).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void 해당하는_예외가_없다면_예외가_발생한다() {
        // given
        Exception exception = new Exception();

        // when, then
        assertThatThrownBy(() -> ExceptionStatusMapper.of(exception))
                .isInstanceOf(IllegalStateException.class);
    }
}
