package cart.order.application.dto;

import cart.order.domain.CartOrder;

import java.util.List;

public class OrderDto {

    private CartOrder cartOrder;
    private List<OrderedProductDto> products;

    public OrderDto(final CartOrder cartOrder, final List<OrderedProductDto> products) {
        this.cartOrder = cartOrder;
        this.products = products;
    }

    public CartOrder getCartOrder() {
        return cartOrder;
    }

    public List<OrderedProductDto> getProducts() {
        return products;
    }
}
