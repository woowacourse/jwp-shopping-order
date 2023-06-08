package cart.domain.order.validator;

import static cart.fixtures.CartItemFixtures.Dooly_CartItem1;
import static cart.fixtures.CartItemFixtures.Dooly_CartItem2;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import cart.domain.cartitem.domain.CartItems;
import cart.domain.order.domain.dto.OrderCartItemDto;
import cart.domain.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderValidatorTest {

    @Nested
    @DisplayName("장바구니 상품 리스트와 OrderCartItemDto 리스트를 받아서 검증 시")
    class validate {

        @Test
        @DisplayName("장바구니 상품 리스트에 있는 장바구니 상품 이름과 OrderCartItemDto 리스트에 있는 상품 이름이 다르면 예외가 발생한다.")
        void throws_not_match_name() {
            // given
            OrderValidator orderValidator = new OrderValidator();
            CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY()));
            Product product1 = Dooly_CartItem1.PRODUCT;
            Product product2 = Dooly_CartItem2.PRODUCT;
            List<OrderCartItemDto> orderCartItemDtos = List.of(
                    new OrderCartItemDto(Dooly_CartItem1.ID, product1.getName() + "UPDATE", product1.getPrice(), product1.getImageUrl()),
                    new OrderCartItemDto(Dooly_CartItem2.ID, product2.getName() + "UPDATE", product2.getPrice(), product2.getImageUrl())
                    );

            // when, then
            assertThatThrownBy(() -> orderValidator.validate(cartItems, orderCartItemDtos))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 상품 정보와 기존 상품 정보가 일치하지 않습니다.");
        }

        @Test
        @DisplayName("장바구니 상품 리스트에 있는 장바구니 상품 가격과 OrderCartItemDto 리스트에 있는 상품 가격이 다르면 예외가 발생한다.")
        void throws_not_match_price() {
            // given
            OrderValidator orderValidator = new OrderValidator();
            CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY()));
            Product product1 = Dooly_CartItem1.PRODUCT;
            Product product2 = Dooly_CartItem2.PRODUCT;
            List<OrderCartItemDto> orderCartItemDtos = List.of(
                    new OrderCartItemDto(Dooly_CartItem1.ID, product1.getName(), product1.getPrice() + 10000, product1.getImageUrl()),
                    new OrderCartItemDto(Dooly_CartItem2.ID, product2.getName(), product2.getPrice() + 10000, product2.getImageUrl())
            );

            // when, then
            assertThatThrownBy(() -> orderValidator.validate(cartItems, orderCartItemDtos))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 상품 정보와 기존 상품 정보가 일치하지 않습니다.");
        }

        @Test
        @DisplayName("장바구니 상품 리스트에 있는 장바구니 상품 이미지와 OrderCartItemDto 리스트에 있는 상품 이미지가 다르면 예외가 발생한다.")
        void throws_not_match_imageUrl() {
            // given
            OrderValidator orderValidator = new OrderValidator();
            CartItems cartItems = new CartItems(List.of(Dooly_CartItem1.ENTITY(), Dooly_CartItem2.ENTITY()));
            Product product1 = Dooly_CartItem1.PRODUCT;
            Product product2 = Dooly_CartItem2.PRODUCT;
            List<OrderCartItemDto> orderCartItemDtos = List.of(
                    new OrderCartItemDto(Dooly_CartItem1.ID, product1.getName(), product1.getPrice(), product1.getImageUrl() + "UPDATE"),
                    new OrderCartItemDto(Dooly_CartItem2.ID, product2.getName(), product2.getPrice(), product2.getImageUrl() + "UPDATE")
            );

            // when, then
            assertThatThrownBy(() -> orderValidator.validate(cartItems, orderCartItemDtos))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 상품 정보와 기존 상품 정보가 일치하지 않습니다.");
        }
    }
}
