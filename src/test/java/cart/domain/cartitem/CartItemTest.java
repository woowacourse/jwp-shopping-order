package cart.domain.cartitem;

import static cart.fixtures.CartItemFixtures.Dooly_CartItem1;
import static cart.fixtures.CartItemFixtures.Dooly_CartItem2;
import static cart.fixtures.ProductFixtures.CHICKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CartItemTest {

    @Test
    @DisplayName("상품을 받아서 장바구니 상품과 같으면 TRUE를 반환한다.")
    void isSameProduct_true() {
        // given
        CartItem cartItem = Dooly_CartItem1.ENTITY();

        // when, then
        assertThat(cartItem.isSameProduct(CHICKEN.ENTITY())).isTrue();
    }

    @Test
    @DisplayName("상품을 받아서 장바구니 상품과 다르면 FALSE를 반환한다.")
    void isSameProduct_false() {
        // given
        CartItem cartItem = Dooly_CartItem2.ENTITY();

        // when, then
        assertThat(cartItem.isSameProduct(CHICKEN.ENTITY())).isFalse();
    }

    @Test
    @DisplayName("받은 상품의 정보가 현재 장바구니 상품과 다르면 TRUE를 반환한다.")
    void isNotSameProductInfo() {
        // given
        CartItem cartItem = Dooly_CartItem1.ENTITY();
        Product product = Dooly_CartItem1.PRODUCT;
        String wrongName = product.getName() + "xxx";
        int wrongPrice = product.getPrice() + 10000;
        String wrongImageUrl = product.getImageUrl() + "xxx";

        // when, then
        assertAll(
                () -> cartItem.isNotSameProductInfo(wrongName, product.getPrice(), product.getImageUrl()),
                () -> cartItem.isNotSameProductInfo(product.getName(), wrongPrice, product.getImageUrl()),
                () -> cartItem.isNotSameProductInfo(product.getName(), product.getPrice(), wrongImageUrl)
        );
    }
}
