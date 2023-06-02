package cart.domain.cartItem.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.cartitem.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class QuantityTest {

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("수량이 1 이상의 양수가 아니면 예외가 발생한다.")
    void validatePositive(int quantity) {
        // when, then
        assertThatThrownBy(() -> new Quantity(quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 양수여야합니다.");

    }

    @Test
    @DisplayName("받은 수량으로 변경된 수량을 반환한다.")
    void changeQuantity() {
        // given
        int currentQuantity = 2;
        Quantity quantity = new Quantity(currentQuantity);
        int quantityToChange = 5;

        // when
        Quantity changedQuantity = quantity.changeQuantity(quantityToChange);

        // then
        assertThat(changedQuantity.getQuantity()).isEqualTo(quantityToChange);
    }

    @Test
    @DisplayName("받은 수량으로 더한 수량을 반환한다.")
    void addQuantity() {
        // given
        int currentQuantity = 2;
        Quantity quantity = new Quantity(currentQuantity);
        int quantityToAdd = 5;
        int addedQuantity = currentQuantity + quantityToAdd;

        // when
        Quantity changedQuantity = quantity.addQuantity(quantityToAdd);

        // then
        assertThat(changedQuantity.getQuantity()).isEqualTo(addedQuantity);
    }
}
