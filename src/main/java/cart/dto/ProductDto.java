package cart.dto;

import cart.domain.Product;

public class ProductDto {

    private final Long id;
    private final String name;
    private final String image;
    private final long price;

    public ProductDto(final Long id, final String name, final String image, final long price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public static ProductDto from(final Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getImage(), product.getPrice());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public long getPrice() {
        return price;
    }
}
