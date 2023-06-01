package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static cart.fixture.CouponFixture.*;
import static org.assertj.core.api.Assertions.*;


class ProductTest {

    public static final Product PRODUCT_1000 = new Product(
            "테스트", 1000, "url");
    public static final Product PRODUCT_1000_500 = new Product(
            "테스트", 1000, "url", 500);
    public static final Product PRODUCT_5000_1000 = new Product("10퍼 할인 상품", 5000, "test", 1000);

    @Test
    @DisplayName("할인 여부를 확인 할 수 있다.")
    void isOnSale() {
        assertThat(PRODUCT_1000.isOnSale()).isFalse();
        assertThat(PRODUCT_1000_500.isOnSale()).isTrue();
    }

    @Test
    @DisplayName("할인 된 가격을 알 수 있다.")
    void salePrice() {
        assertThat(PRODUCT_1000.getDiscountedPrice()).isEqualTo(new Price(1000));
        assertThat(PRODUCT_1000_500.getDiscountedPrice()).isEqualTo(new Price(500));
    }

    @Test
    @DisplayName("상품에 쿠폰을 적용 할 수 있다.")
    void applyCoupon() {
        Price price = PRODUCT_1000.applyCoupon(PRODUCT_COUPON1_Percent_10);

        assertThat(price).isEqualTo(new Price(900));
    }

    @Test
    @DisplayName("할인된 상품에 쿠폰을 적용 할 수 있다.")
    void applyCoupon1() {
        Price price = PRODUCT_1000_500.applyCoupon(PRODUCT_COUPON1_Percent_10);

        assertThat(price).isEqualTo(new Price(450));
    }

}