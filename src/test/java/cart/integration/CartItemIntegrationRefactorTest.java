package cart.integration;

import static cart.TestDataFixture.MEMBER_1_AUTH_HEADER;
import static cart.TestDataFixture.PRODUCT_1;
import static cart.TestDataFixture.objectToJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.domain.product.CartItem;
import cart.domain.product.Product;
import cart.domain.vo.Quantity;
import cart.service.request.CartItemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class CartItemIntegrationRefactorTest extends IntegrationRefactorTest {

    @DisplayName("상품의 개수만큼 한번에 장바구니에 담는 기능 추가")
    @Test
    void addProductQuantity() throws Exception {
        final Product savedProduct = productRepository.insertProduct(PRODUCT_1);
        final CartItemRequest cartItemRequest = new CartItemRequest(savedProduct.getId(), 3);

        final String location = mockMvc
                .perform(post("/cart-items")
                        .header("Authorization", MEMBER_1_AUTH_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJsonString(cartItemRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        final Long orderId = Long.parseLong(location.split("/cart-items/")[1]);
        final CartItem cartItem = cartItemDao.findById(orderId);
        assertThat(cartItem)
                .extracting(CartItem::getProduct, CartItem::getQuantity)
                .containsExactly(savedProduct, new Quantity(3));
    }
}
