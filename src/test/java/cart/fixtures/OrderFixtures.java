package cart.fixtures;

import static cart.fixtures.CartItemFixtures.Dooly_CartItem1;
import static cart.fixtures.CartItemFixtures.Dooly_CartItem2;

import java.time.LocalDateTime;
import java.util.List;

import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.product.Product;
import cart.dto.OrderCartItemDto;
import cart.dto.OrderRequest;
import cart.fixtures.MemberFixtures.Dooly;

public class OrderFixtures {

    public static class Dooly_Order1 {
        public static final Long ID = 1L;
        public static final Member MEMBER = Dooly.ENTITY();
        public static final int TOTAL_PRICE = 20000;
        public static final LocalDateTime CREATED_AT = LocalDateTime.of(2023, 5, 31, 13, 30);
        public static Order DOMAIN() {
            return new Order(null, MEMBER, TOTAL_PRICE, CREATED_AT);
        }
        public static Order ENTITY() {
            return new Order(ID, MEMBER, TOTAL_PRICE, CREATED_AT);
        }

        public static OrderRequest REQUEST() {
            CartItem cartItem1 = Dooly_CartItem1.ENTITY();
            Product product1 = Dooly_CartItem1.PRODUCT;
            CartItem cartItem2 = Dooly_CartItem2.ENTITY();
            Product product2 = Dooly_CartItem2.PRODUCT;

            List<OrderCartItemDto> orderCartItemDtos = List.of(
                    new OrderCartItemDto(cartItem1.getId(), product1.getName(), product1.getPrice(), product1.getImageUrl()),
                    new OrderCartItemDto(cartItem2.getId(), product2.getName(), product2.getPrice(), product2.getImageUrl())
            );
            return new OrderRequest(orderCartItemDtos);
        }

        public static OrderRequest UPDATE_NAME_REQUEST() {
            CartItem cartItem1 = Dooly_CartItem1.ENTITY();
            Product product1 = Dooly_CartItem1.PRODUCT;
            CartItem cartItem2 = Dooly_CartItem2.ENTITY();
            Product product2 = Dooly_CartItem2.PRODUCT;

            List<OrderCartItemDto> orderCartItemDtos = List.of(
                    new OrderCartItemDto(cartItem1.getId(), product1.getName(), product1.getPrice(), product1.getImageUrl()),
                    new OrderCartItemDto(cartItem2.getId(), product2.getName() + "update", product2.getPrice(), product2.getImageUrl())
            );
            return new OrderRequest(orderCartItemDtos);
        }

        public static OrderRequest UPDATE_PRICE_REQUEST() {
            CartItem cartItem1 = Dooly_CartItem1.ENTITY();
            Product product1 = Dooly_CartItem1.PRODUCT;
            CartItem cartItem2 = Dooly_CartItem2.ENTITY();
            Product product2 = Dooly_CartItem2.PRODUCT;

            List<OrderCartItemDto> orderCartItemDtos = List.of(
                    new OrderCartItemDto(cartItem1.getId(), product1.getName(), product1.getPrice(), product1.getImageUrl()),
                    new OrderCartItemDto(cartItem2.getId(), product2.getName(), product2.getPrice() + 10000, product2.getImageUrl())
            );
            return new OrderRequest(orderCartItemDtos);
        }

        public static OrderRequest UPDATE_IMAGE_URL_REQUEST() {
            CartItem cartItem1 = Dooly_CartItem1.ENTITY();
            Product product1 = Dooly_CartItem1.PRODUCT;
            CartItem cartItem2 = Dooly_CartItem2.ENTITY();
            Product product2 = Dooly_CartItem2.PRODUCT;

            List<OrderCartItemDto> orderCartItemDtos = List.of(
                    new OrderCartItemDto(cartItem1.getId(), product1.getName(), product1.getPrice(), product1.getImageUrl()),
                    new OrderCartItemDto(cartItem2.getId(), product2.getName(), product2.getPrice(), product2.getImageUrl() + "update")
            );
            return new OrderRequest(orderCartItemDtos);
        }
    }
}
