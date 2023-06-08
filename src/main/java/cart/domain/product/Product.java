package cart.domain.product;

import cart.application.service.product.dto.ProductCommandDto;

public class Product {
    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;

    public Product(final ProductCommandDto productCreateDto) {
        this(null, productCreateDto.getName(), productCreateDto.getPrice(), productCreateDto.getImageUrl());
    }

    public Product(final String name, final int price, final String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public Product(final Long productId, final ProductCommandDto productCreateDto) {
        this(productId, productCreateDto.getName(), productCreateDto.getPrice(), productCreateDto.getImageUrl());
    }

    public Product(final Long id, final String name, final int price, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }


    public int calculatePriceBy(final int quantity) {
        return quantity * price;
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

    public boolean isEqualId(final long productId) {
        return id == productId;
    }

}
