package cart.domain;

import java.math.BigDecimal;

public class Product {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final String imageUrl;

    public Product(String name, int price, String imageUrl) {
        this(name, BigDecimal.valueOf(price), imageUrl);
    }

    public Product(String name, BigDecimal price, String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public Product(Long id, String name, BigDecimal price, String imageUrl) {
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

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
