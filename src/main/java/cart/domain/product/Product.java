package cart.domain.product;

import cart.exception.SalePercentageInvalidRangeException;

import java.util.Objects;

public class Product {

    private static final int MAXIMUM_SALE_PERCENT = 100;
    private static final int MINIMUM_SALE_PERCENT = 1;
    private static final int DEFAULT_SALE_PRICE = 0;

    private Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private Integer salePrice;

    public Product(final String name, final int price, final String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        salePrice = 0;
    }

    public Product(final Long id, final String name, final int price, final String imageUrl, final Integer salePrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.salePrice = salePrice;
    }

    public int applySale(int percentage) {
        if (validateRangeOfSalePercentage(percentage)) {
            throw new SalePercentageInvalidRangeException(percentage);
        }

        salePrice = (int) (price * percentage * 0.01);
        return salePrice;
    }

    private boolean validateRangeOfSalePercentage(final int value) {
        return value > MAXIMUM_SALE_PERCENT || value < MINIMUM_SALE_PERCENT;
    }

    public void unApplySale() {
        this.salePrice = DEFAULT_SALE_PRICE;
    }

    public int getAppliedDiscountOrOriginPrice() {
        if (salePrice == null) {
            return price;
        }

        return price - salePrice;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public boolean isSame(final Long id) {
        return Objects.equals(this.id, id);
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
