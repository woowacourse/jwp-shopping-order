package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.order.OutOfStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Product 단위테스트")
class ProductTest {

    @Test
    void product는_이름과_가격과_이미지주소와_재고를_가진다() {
        // given
        // when
        Product chicken = new Product("치킨", 5000, "http://chickenimageurl", 30);

        // then
        assertThat(chicken.getName()).isEqualTo("치킨");
        assertThat(chicken.getPrice()).isEqualTo(5000);
        assertThat(chicken.getImageUrl()).isEqualTo("http://chickenimageurl");
        assertThat(chicken.getStock()).isEqualTo(30);
    }

    @Test
    void 입력받은_수량만큼_판매될_수_있다() {
        // given
        Product chicken = new Product("치킨", 5000, "http://chickenimageurl", 30);

        // when
        chicken.sold(5);

        // then
        assertThat(chicken.getStock()).isEqualTo(25);
    }

    @Test
    void 입력받은_수량이_재고보다_많으면_예외가_발생한다() {
        // given
        Product chicken = new Product("치킨", 5000, "http://chickenimageurl", 30);

        // when
        // then
        assertThatThrownBy(() -> chicken.sold(50))
                .isInstanceOf(OutOfStockException.class);
    }
}
