package cart.domain.cart;

import cart.domain.VO.Money;
import cart.exception.cart.ProductNotValidException;
import java.util.Objects;

public class Product {

    private static final int MAX_NAME_LENGTH = 100;
    private static final int MIN_PRICE_VALUE = 0;

    private final Long id;
    private final String name;
    private final String imageUrl;
    private final Money price;

    public Product(final String name, final String imageUrl, final long price) {
        this(null, name, imageUrl, price);
    }

    public Product(final Long id, final String name, final String imageUrl, final long price) {
        validate(name, imageUrl, price);
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = Money.from(price);
    }

    private void validate(final String name, final String imageUrl, final long price) {
        validateName(name);
        validateImage(imageUrl);
        validatePrice(price);
    }

    private void validateName(final String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new ProductNotValidException("상품명의 길이는 " + MAX_NAME_LENGTH + "자 이하여야합니다.");
        }
    }

    private void validateImage(final String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new ProductNotValidException("이미지는 공백일 수 없습니다.");
        }
    }

    private void validatePrice(final long price) {
        if (price < MIN_PRICE_VALUE) {
            throw new ProductNotValidException("상품 가격은 " + MIN_PRICE_VALUE + "원 이상이여야 합니다.");
        }
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Money getPrice() {
        return price;
    }
}
