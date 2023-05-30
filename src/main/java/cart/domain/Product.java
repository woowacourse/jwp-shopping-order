package cart.domain;

import cart.domain.vo.Amount;

public class Product {

    private final Long id;
    private final String name;
    private final Amount amount;
    private final String imageUrl;

    public Product(final String name, final Amount amount, final String imageUrl) {
        this(null, name, amount, imageUrl);
    }

    public Product(final Long id, final String name, final Amount amount, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Amount getAmount() {
        return amount;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
