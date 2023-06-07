package cart.product.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class ProductTest {
    public static final Product PRODUCT_FIRST = new Product(1L, "치킨", 10000L, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80", 10.0, true);
    public static final Product PRODUCT_SECOND = new Product(2L, "샐러드", 20000L, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80", 10.0, false);
    @Test
    void 포인트가_적립될_상품이면_적립금을_계산한다() {
        // when
        final long actualPointToAdd = PRODUCT_FIRST.calculatePointToAdd();
        
        // then
        assertThat(actualPointToAdd).isEqualTo(1000);
    }

    @Test
    void 해당_물품에_적용_가능한_최대_포인트를_구한다() {
        // when
        final Long availablePoint = PRODUCT_FIRST.calculateAvailablePoint();

        // then
        assertThat(availablePoint).isEqualTo(10000L);
    }
}
