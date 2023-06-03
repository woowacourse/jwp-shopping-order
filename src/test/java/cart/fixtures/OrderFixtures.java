package cart.fixtures;

import static cart.fixtures.CartItemFixtures.*;
import static cart.fixtures.ProductFixtures.*;

import java.time.LocalDateTime;
import java.util.List;

import cart.domain.cartitem.domain.CartItem;
import cart.domain.member.domain.Member;
import cart.domain.order.application.dto.OrderRequest;
import cart.domain.order.application.dto.OrderResponse;
import cart.domain.order.domain.Order;
import cart.domain.order.domain.dto.OrderCartItemDto;
import cart.domain.order.domain.dto.OrderedProductDto;
import cart.domain.product.application.dto.ProductResponse;
import cart.domain.product.domain.Product;
import cart.fixtures.MemberFixtures.Dooly;

public class OrderFixtures {

    public static class Dooly_Order1 {
        public static final Long ID = 1L;
        public static final Member MEMBER = Dooly.ENTITY();
        public static final int TOTAL_PRICE = 100000;
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

        public static OrderResponse RESPONSE() {
            ProductResponse productResponse1 = CHICKEN.RESPONSE;
            OrderedProductDto orderedProductDto1 = new OrderedProductDto(productResponse1, Dooly_CartItem1.QUANTITY);
            ProductResponse productResponse2 = SALAD.RESPONSE;
            OrderedProductDto orderedProductDto2 = new OrderedProductDto(productResponse2, Dooly_CartItem2.QUANTITY);
            return new OrderResponse(ID, CREATED_AT, List.of(orderedProductDto2, orderedProductDto1), TOTAL_PRICE);
        }
    }

    public static class Dooly_Order2 {
        public static final Long ID = 2L;
        public static final Member MEMBER = Dooly.ENTITY();
        public static final int TOTAL_PRICE = 40000;
        public static final LocalDateTime CREATED_AT = LocalDateTime.of(2023, 5, 31, 13, 30);

        public static Order DOMAIN() {
            return new Order(null, MEMBER, TOTAL_PRICE, CREATED_AT);
        }

        public static Order ENTITY() {
            return new Order(ID, MEMBER, TOTAL_PRICE, CREATED_AT);
        }

        public static OrderRequest REQUEST() {
            CartItem cartItem1 = Dooly_CartItem3.ENTITY();
            Product product1 = PANCAKE.ENTITY();

            List<OrderCartItemDto> orderCartItemDtos = List.of(
                    new OrderCartItemDto(cartItem1.getId(), product1.getName(), product1.getPrice(), product1.getImageUrl())
            );
            return new OrderRequest(orderCartItemDtos);
        }

        public static OrderResponse RESPONSE() {
            ProductResponse productResponse1 = PANCAKE.RESPONSE;
            OrderedProductDto orderedProductDto1 = new OrderedProductDto(productResponse1, Dooly_CartItem1.QUANTITY);
            return new OrderResponse(ID, CREATED_AT, List.of(orderedProductDto1), TOTAL_PRICE);
        }
    }
}
