package cart.domain;

import cart.exception.InvalidImageUrlException;
import cart.exception.InvalidPriceException;
import cart.exception.InvalidProductNameException;

import java.net.MalformedURLException;
import java.net.URL;

public class Product {
    private static final int MINIMUM_NAME_LENGTH = 1;
    private static final int MAXIMUM_NAME_LENGTH = 255;
    private static final int MINIMUM_PRICE = 1;

    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    public Product(String name, int price, String imageUrl) {
        validateName(name);
        validatePrice(price);
        validateImageUrl(imageUrl);
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    private void validateImageUrl(String imageUrl) {
        try {
            new URL(imageUrl);
        } catch (MalformedURLException e) {
            throw new InvalidImageUrlException("잘못된 이미지 url입니다.");
        }
    }

    private void validatePrice(int price) {
        if (price < MINIMUM_PRICE) {
            throw new InvalidPriceException("상품 가격은 " + MINIMUM_PRICE + " 이상이어야 합니다.");
        }
    }

    private void validateName(String name) {
        if (name.length() < MINIMUM_NAME_LENGTH || name.length() > MAXIMUM_NAME_LENGTH) {
            throw new InvalidProductNameException("상품 이름은 " + MINIMUM_NAME_LENGTH + "이상 " + MAXIMUM_NAME_LENGTH + "이하여야합니다.");
        }
    }

    public Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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
