package cart.domain.order;

import cart.domain.Product;

public class OrderItem {

    private final Long id;
    private final String productName;
    private final int productPrice;
    private final int productQuantity;
    private final String productImage;

    public OrderItem(Long id, final String productName, final String productImage, final int productPrice,
                     final int productQuantity) {
        this.id = id;

        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    public static OrderItem of(final int quantity, final Product product) {
        return new OrderItem(
                null,
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                quantity
        );
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

}
