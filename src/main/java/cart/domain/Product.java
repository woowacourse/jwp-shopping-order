package cart.domain;

import cart.exception.OrderException;
import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private int stock;

    public Product(String name, int price, String imageUrl, int stock) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public Product(Long id, String name, int price, String imageUrl, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public void sold(int quantity) {
        if (this.stock < quantity) {
            throw new OrderException("상품 재고가 부족합니다.");
        }
        this.stock -= quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return price == product.price && id.equals(product.id) && name.equals(product.name) && imageUrl.equals(
                product.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", stock=" + stock +
                '}';
    }
}
