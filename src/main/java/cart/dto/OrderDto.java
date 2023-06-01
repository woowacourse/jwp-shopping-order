package cart.dto;

import cart.domain.CartOrder;
import cart.domain.Product;

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
