package cart.domain.product;

import cart.domain.Money;
import cart.exception.ExceptionType;
import cart.exception.ProductException;
import java.math.BigDecimal;
import java.util.Objects;

public class Product {

    private final Long id;
    private ProductName name;
    private Money price;
    private ImageUrl imageUrl;

    public Product(String name, int price, String imageUrl) {
        this(name, BigDecimal.valueOf(price), imageUrl);
    }

    public Product(String name, BigDecimal price, String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public Product(Long id, String name, BigDecimal price, String imageUrl) {
        this(id, name, new Money(price), imageUrl);
    }

    public Product(Long id, String name, Money price, String imageUrl) {
        this(id, new ProductName(name), price, new ImageUrl(imageUrl));
    }

    public Product(Long id, ProductName name, Money price, ImageUrl imageUrl) {
        validateMoney(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    private void validateMoney(Money price) {
        if (price.isLessThan(BigDecimal.valueOf(1000))) {
            throw new ProductException(ExceptionType.INVALID_PRODUCT_PRICE);
        }
    }

    public void update(String name, BigDecimal price, String imageUrl) {
        this.name = new ProductName(name);
        this.price = new Money(price);
        this.imageUrl = new ImageUrl(imageUrl);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public Money getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl.getValue();
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
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
