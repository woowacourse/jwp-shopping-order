package cart.domain;

import cart.domain.vo.Money;
import cart.domain.vo.Name;
import cart.exception.ProductException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class Product {

    public static final int MINIMUM_NAME_LENGTH = 1;
    public static final int MAXIMUM_NAME_LENGTH = 255;
    public static final int MINIMUM_PRICE = 1;

    private final Long id;
    private Name name;
    private Money price;
    private String imageUrl;

    public Product(Long id, String name, int price, String imageUrl) {
        this(id, Name.from(name), Money.from(price), imageUrl);
    }

    public Product(String name, int price, String imageUrl) {
        this(null, Name.from(name), Money.from(price), imageUrl);
    }

    public Product(Name name, Money price, String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public Product(Long id, Name name, Money price, String imageUrl) {
        validate(name, imageUrl);
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    private void validate(Name name, String imageUrl) {
        if (name.getValue().length() < MINIMUM_NAME_LENGTH || name.getValue().length() > MAXIMUM_NAME_LENGTH) {
            throw new ProductException.InvalidNameLength();
        }
        try {
            new URL(imageUrl);
        } catch (MalformedURLException e) {
            throw new ProductException.InvalidImageUrl();
        }
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Money getPrice() {
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
        return Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(imageUrl, product.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, imageUrl);
    }
}
