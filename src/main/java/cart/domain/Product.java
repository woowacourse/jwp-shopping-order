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
        validateId(id);
        this.id = id;

        validateName(name);
        this.name = name;

        validatePrice(price);
        this.price = price;

        validateImageUrl(imageUrl);
        this.imageUrl = imageUrl;
    }

    public Product(String name, int price, String imageUrl) {
        validateName(name);
        this.name = name;

        validatePrice(price);
        this.price = price;

        validateImageUrl(imageUrl);
        this.imageUrl = imageUrl;
    }

    private void validateId(Long id) {
        if (Objects.isNull(id)) {
            throw new ProductException.InvalidId("상품 아이디를 입력해야 합니다.");
        }
    }

    private void validateName(String name) {
        if (name.length() < MINIMUM_NAME_LENGTH || name.length() > MAXIMUM_NAME_LENGTH) {
            throw new ProductException.InvalidName("상품 이름은 " + MINIMUM_NAME_LENGTH + "자 이상 " + MAXIMUM_NAME_LENGTH + "자 이하여야합니다.");
        }
    }

    private void validatePrice(int price) {
        if (price < MINIMUM_PRICE) {
            throw new ProductException.InvalidPrice("상품 가격은 " + MINIMUM_PRICE + "원 이상이어야 합니다.");
        }
    }

    private void validateImageUrl(String imageUrl) {
        try {
            new URL(imageUrl);
        } catch (MalformedURLException e) {
            throw new ProductException.InvalidImageUrl("잘못된 이미지 url입니다.");
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
