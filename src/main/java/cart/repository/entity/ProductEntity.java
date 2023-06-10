package cart.repository.entity;

import java.util.Objects;

public class ProductEntity {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;

    private ProductEntity(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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

    public static class Builder {

        private Long id;
        private String name;
        private int price;
        private String imageUrl;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(int price) {
            this.price = price;
            return this;
        }


        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ProductEntity build() {
            return new ProductEntity(id, name, price, imageUrl);
        }
    }
}
