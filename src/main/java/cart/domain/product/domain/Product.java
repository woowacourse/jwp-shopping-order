package cart.domain.product.domain;

import java.util.Objects;

public class Product {
    private Long id;
    private final ProductName productName;
    private final ProductPrice productPrice;
    private final ProductImageUrl productImageUrl;

    public Product(String name, int price, String imageUrl) {
        this.productName = new ProductName(name);
        this.productPrice = new ProductPrice(price);
        this.productImageUrl = new ProductImageUrl(imageUrl);
    }

    public Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.productName = new ProductName(name);
        this.productPrice = new ProductPrice(price);
        this.productImageUrl = new ProductImageUrl(imageUrl);
    }

    public boolean isSameName(String name) {
        return this.productName.equals(new ProductName(name));
    }

    public boolean isSamePrice(int price) {
        return this.productPrice.equals(new ProductPrice(price));
    }

    public boolean isSameImageUrl(String imageUrl) {
        return this.productImageUrl.equals(new ProductImageUrl(imageUrl));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return productName.getName();
    }

    public int getPrice() {
        return productPrice.getPrice();
    }

    public String getImageUrl() {
        return productImageUrl.getImageUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(productName, product.productName) && Objects.equals(productPrice, product.productPrice) && Objects.equals(productImageUrl, product.productImageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, productPrice, productImageUrl);
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
