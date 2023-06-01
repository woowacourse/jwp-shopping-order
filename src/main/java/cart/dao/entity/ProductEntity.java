package cart.dao.entity;

import cart.domain.product.Product;
import cart.domain.vo.Money;
import cart.domain.vo.Name;

import java.util.Objects;

public class ProductEntity {

    private final Long id;
    private final String name;
    private final int money;
    private final String imageUrl;

    public ProductEntity(String name, int money, String imageUrl) {
        this(null, name, money, imageUrl);
    }

    public ProductEntity(Long id, String name, int money, String imageUrl) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.imageUrl = imageUrl;
    }

    public Product toDomain() {
        return new Product(id, Name.from(name), Money.from(money), imageUrl);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
