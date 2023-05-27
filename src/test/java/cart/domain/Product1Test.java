package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Product1Test {

    public static final Product1 PRODUCT_1 = new Product1("테스트", new Price(1000), "url");
    public static final Product1 PRODUCT_2 = new Product1("테스트", new Price(1000), "url", new Percentage(10));

    @Test
    @DisplayName("할인 여부를 확인 할 수 있다.")
    void isOnSale() {
        assertThat(PRODUCT_1.isOnSale()).isFalse();
        assertThat(PRODUCT_2.isOnSale()).isTrue();
    }

    @Test
    @DisplayName("할인 된 가격을 알 수 있다.")
    void salePrice() {
        assertThat(PRODUCT_1.getSalePrice()).isEqualTo(new Price(1000));
        assertThat(PRODUCT_2.getSalePrice()).isEqualTo(new Price(900));
    }


}