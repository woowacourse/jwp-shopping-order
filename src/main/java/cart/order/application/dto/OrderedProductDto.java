package cart.order.application.dto;

import cart.product.domain.Product;

public class OrderedProductDto {

    private Product product;
    private int quantity;

    public OrderedProductDto(final Product product, final int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static OrderedProductDto from(final OrderItemDto orderItemDto) {
        final Product product = Product.of(orderItemDto.getProductId(), orderItemDto.getName(), orderItemDto.getPrice(), orderItemDto.getImageUrl());
        final int quantity = orderItemDto.getQuantity();

        return new OrderedProductDto(product, quantity);
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
