package cart.entity;

import cart.domain.Product;

public class ProductEntity {
    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final Boolean isDeleted;

    public ProductEntity(String name, int price, String imageUrl) {
        this(null, name, price, imageUrl, null);
    }

    public ProductEntity(Long id, String name, int price, String imageUrl) {
        this(id, name, price, imageUrl, null);
    }

    public ProductEntity(final Long id, final String name, final int price, final String imageUrl, final Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isDeleted = isDeleted;
    }

    public static ProductEntity from(Product insertProduct) {
        return new ProductEntity(insertProduct.getId(),
                insertProduct.getName(),
                insertProduct.getPrice(),
                insertProduct.getImageUrl());
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

    public Boolean isDeleted() {
        return isDeleted;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }

}
