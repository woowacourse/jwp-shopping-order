package cart.domain.order;

import cart.domain.Product;

public class OrderItem {

    private final String productName;
    private final String productImage;
    private final int productPrice;
    private final int productQuantity;

    public OrderItem(final String productName, final String productImage, final int productPrice, final int productQuantity) {
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    public static OrderItem of(final int quantity, final Product product) {
        return new OrderItem(
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                quantity
        );
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
