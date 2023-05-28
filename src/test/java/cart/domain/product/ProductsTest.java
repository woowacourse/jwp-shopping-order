package cart.domain.product;

import static cart.fixtures.ProductFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import cart.domain.cartitem.CartItems;
import cart.dto.ProductCartItemResponse;
import cart.fixtures.CartItemFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductsTest {

    @Test
    @DisplayName("장바구니 상품을 받아서 상품 조회 목록을 반환한다.")
    void getProductCartItems() {
        // given
        CartItems cartItems = new CartItems(List.of(CartItemFixtures.Dooly_CartItem1.ENTITY, CartItemFixtures.Dooly_CartItem2.ENTITY));
        Products products = new Products(List.of(CHICKEN.ENTITY, SALAD.ENTITY, PIZZA.ENTITY));

        // when
        List<ProductCartItemResponse> productCartItems = products.getProductCartItems(cartItems);

        // then
        assertThat(productCartItems).usingRecursiveComparison().isEqualTo(FIRST_PAGING_PRODUCTS.PRODUCT_CART_ITEM_RESPONSES);
    }
}
