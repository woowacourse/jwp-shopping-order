package cart.domain.product;

import cart.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static cart.exception.ErrorCode.INVALID_ITEM_QUANTITY_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ItemTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("치킨", 1000, "http://example.com/chicken.jpg");
    }

    @DisplayName("Item이 정상적으로 생성된다.")
    @Test
    void item() {
        // when & then
        assertDoesNotThrow(() -> new Item(product));
    }

    @DisplayName("Item이 생성될 때, 수량이 1개 미만 1000개 초과 시 INVALID_ITEM_QUANTITY_SIZE 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1001})
    void item_ValidateQuantity(int quantity) {
        // when & then
        assertThatThrownBy(() -> new Item(product, quantity))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(INVALID_ITEM_QUANTITY_SIZE);
    }

    @DisplayName("Item의 전체 수량과 상품 가격을 계산한다.")
    @Test
    void calculateItemPrice() {
        // given
        Item item = new Item(product, 10);

        // when
        int price = item.calculateItemPrice();

        // then
        assertThat(price).isEqualTo(10_000);
    }

}
