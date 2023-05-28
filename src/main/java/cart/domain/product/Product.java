package cart.domain.product;

import java.util.Objects;

public class Product {

    private final Long id;
    private final ProductName name;
    private final ProductPrice price;
    private final ProductImageUrl imageUrl;

    public Product(final String name, final int price, final String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public Product(final Long id, final String name, final int price, final String imageUrl) {
        this.id = id;
        this.name = new ProductName(name);
        this.price = new ProductPrice(price);
        this.imageUrl = new ProductImageUrl(imageUrl);
    }

    public Long getId() {
        return id;
    }

    public String getNameValue() {
        return name.getName();
    }

    public int getPriceValue() {
        return price.getPrice();
    }

    public String getImageUrlValue() {
        return imageUrl.getImageUrl();
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
}
