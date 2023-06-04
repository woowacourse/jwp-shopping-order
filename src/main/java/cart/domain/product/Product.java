package cart.domain.product;

import java.util.Objects;

public class Product {

    private final Long id;
    private final ProductName productName;
    private final ProductPrice productPrice;
    private final ProductImageUrl productImageUrl;

    public Product(final ProductName productName, final ProductPrice productPrice, final ProductImageUrl productImageUrl) {
        this(-1, productName, productPrice, productImageUrl);
    }

    public Product(final long id, final ProductName productName,
                   final ProductPrice productPrice, final ProductImageUrl productImageUrl) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getNameValue() {
        return productName.getName();
    }

    public ProductName getProductName() {
        return productName;
    }

    public int getPriceValue() {
        return productPrice.getPrice();
    }

    public ProductPrice getProductPrice() {
        return productPrice;
    }

    public String getImageUrlValue() {
        return productImageUrl.getImageUrl();
    }

    public ProductImageUrl getProductImageUrl() {
        return productImageUrl;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName=" + productName +
                ", productPrice=" + productPrice +
                ", productImageUrl=" + productImageUrl +
                '}';
    }
}
