package cart.domain.Product;

import cart.domain.Member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    @DisplayName("상품 생성시 이름, 가격, 이미지 주소가 포맷에 맞으면 오류가 발생하지 않는다.")
    void createProductWithCorrectFormat(){
        assertDoesNotThrow(() -> Product.from("name", 10, "http://example.com/pizza.jpg"));
    }

    @ParameterizedTest()
    @CsvSource(value = {
            "'',10,http://example.com/pizza.jpg",
            "상품,-1,http://example.com/pizza.jpg",
            "상품,10,정확하지 않은 URL"
            }, delimiter = ',')
    @DisplayName("상품 생성시 이름, 가격, 이미지 주소 포맷이 맞지 않으면 오류가 발생한다.")
    void shouldThrowExceptionWhenCreateProductWithNotFormat(
            String name,
            int price,
            String imageUrl
    ){
        assertThatThrownBy(()-> Product.from(name, price, imageUrl))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}