package cart.domain.product;

import cart.domain.vo.Price;
import cart.exception.ProductException;

import java.net.MalformedURLException;
import java.net.URL;

public class Product {

    public static final int MINIMUM_NAME_LENGTH = 1;
    public static final int MAXIMUM_NAME_LENGTH = 255;

    private Long id;
    private final String name;
    private final Price price;
    private final String imageUrl;

    public Product(long id, String name, int price, String imageUrl) {
        validate(name, imageUrl);
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.imageUrl = imageUrl;
    }

    public Product(String name, int price, String imageUrl) {
        validate(name, imageUrl);
        this.name = name;
        this.price = new Price(price);
        this.imageUrl = imageUrl;
    }

    private void validate(String name, String imageUrl) {
        validateName(name);
        validateImageUrl(imageUrl);
    }

    private void validateName(String name) {
        if (name.length() < MINIMUM_NAME_LENGTH || name.length() > MAXIMUM_NAME_LENGTH) {
            throw new ProductException.InvalidNameLength();
        }
    }

    private void validateImageUrl(String imageUrl) {
        try {
            new URL(imageUrl);
        } catch (MalformedURLException e) {
            throw new ProductException.InvalidImageUrl();
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price.getPrice();
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
