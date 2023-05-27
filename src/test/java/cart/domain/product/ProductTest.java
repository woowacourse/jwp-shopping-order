package cart.domain.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.ProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {

    @Nested
    @DisplayName("상품 생성 시 ")
    class CreateProduct {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("상품 이름이 존재하지 않으면 예외를 던진다.")
        void emptyName(String name) {
            assertThatThrownBy(() -> new Product(1L, name, 500, "http://shopping.com"))
                    .isInstanceOf(ProductException.class)
                    .hasMessage("상품 이름은 존재해야 합니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {256, 1024})
        @DisplayName("상품 이름 길이가 최대 길이를 초과하면 예외를 던진다.")
        void overLengthName(int nameLength) {
            String name = "a".repeat(nameLength);

            assertThatThrownBy(() -> new Product(1L, name, 500, "http://shopping.com"))
                    .isInstanceOf(ProductException.class)
                    .hasMessage("상품 이름은 최대 255글자까지 가능합니다. 현재 길이: " + name.length());
        }

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -1})
        @DisplayName("상품 가격이 음수라면 예외를 던진다.")
        void negativePrice(int price) {
            assertThatThrownBy(() -> new Product(1L, "사과", price, "http://shopping.com"))
                    .isInstanceOf(ProductException.class)
                    .hasMessage("상품 가격은 음수일 수 없습니다.");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("상품 이미지 URL이 존재하지 않으면 예외를 던진다.")
        void emptyImageUrl(String imageUrl) {
            assertThatThrownBy(() -> new Product(1L, "사과", 500, imageUrl))
                    .isInstanceOf(ProductException.class)
                    .hasMessage("상품 이미지는 존재해야 합니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {513, 1024})
        @DisplayName("상품 이미지 URL이 최대 길이를 초과하면 예외를 던진다.")
        void overLengthImageUrl(int imageUrlLength) {
            String imageUrl = "a".repeat(imageUrlLength);

            assertThatThrownBy(() -> new Product(1L, "사과", 500, imageUrl))
                    .isInstanceOf(ProductException.class)
                    .hasMessage("상품 이미지 URL은 최대 512글자까지 가능합니다. 현재 길이: " + imageUrl.length());
        }
    }
}
