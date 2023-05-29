package cart.domain;

import cart.exception.ProductException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class Product {

    public static final int MINIMUM_NAME_LENGTH = 1;
    public static final int MAXIMUM_NAME_LENGTH = 255;
    public static final int MINIMUM_PRICE = 1;

    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    public Product(Long id, String name, int price, String imageUrl) {
        validate(id, name, price, imageUrl);
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(String name, int price, String imageUrl) {
        validate(name, price, imageUrl);
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    private void validate(String name, int price, String imageUrl) {
        validateName(name);
        validatePrice(price);
        validateImageUrl(imageUrl);
    }

    private void validate(Long id, String name, int price, String imageUrl) {
        validateId(id);
        validate(name, price, imageUrl);
    }

    private void validateId(Long id) {
        if (Objects.isNull(id)) {
            throw new ProductException.InvalidIdByNull();
        }
    }

    private void validateName(String name) {
        if (name.length() < MINIMUM_NAME_LENGTH || name.length() > MAXIMUM_NAME_LENGTH) {
            throw new ProductException.InvalidNameLength();
        }
    }

    private void validatePrice(int price) {
        if (price < MINIMUM_PRICE) {
            throw new ProductException.InvalidPrice();
        }
    }

    private void validateImageUrl(String imageUrl) {
        try {
            new URL(imageUrl);
        } catch (MalformedURLException e) {
            throw new ProductException.InvalidImageUrl();
        }
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
}
