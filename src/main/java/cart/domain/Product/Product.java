package cart.domain.Product;

import java.util.Objects;

public class Product {
    private Long id;
    private Name name;
    private Price price;
    private ImageUrl imageUrl;

    private Product(Long id, Name name, Price price, ImageUrl imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(Long id, String name, int price, String imageUrl) {
        this(id, new Name(name), new Price(price), new ImageUrl(imageUrl));
    }

    public static Product from(String name, int price, String imageUrl) {
        return new Product(null, Name.from(name), Price.from(price), ImageUrl.from(imageUrl));
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public ImageUrl getImageUrl() {
        return imageUrl;
    }

    public Price multiplyPriceBy(int quantity) {
        return price.multiply(quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
