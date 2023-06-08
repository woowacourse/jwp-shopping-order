package cart.domain;

import java.util.Objects;

public class Product {

    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;

    private Product(Long id, String name, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static Product createInitProduct(String name, Integer price, String imageUrl) {
        return new Product(null, name, price, imageUrl);
    }

    public static Product of(Long id, String name, Integer price, String imageUrl) {
        return new Product(id, name, price, imageUrl);
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(imageUrl, product.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, imageUrl);
    }
}
