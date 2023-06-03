package cart.dto;

import cart.domain.order.OrderItem;

public class OrderedProduct {

    private String name;
    private Integer price;
    private Integer quantity;
    private String imageUrl;

    public OrderedProduct() {
    }

    public OrderedProduct(final String name, final Integer price, final Integer quantity, final String imageUrl) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public static OrderedProduct from(final OrderItem orderItem) {
        return new OrderedProduct(
                orderItem.getProductNameValue(),
                orderItem.getProductPriceValue(),
                orderItem.getQuantity(),
                orderItem.getProductImageUrlValue()
        );
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
