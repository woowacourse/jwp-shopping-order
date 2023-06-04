package cart.domain.product;

import cart.domain.discount.Policy;
import cart.domain.discount.PolicyPercentage;
import cart.exception.SalePercentageInvalidRangeException;

import java.util.Objects;

public class Product {

    private static final int MAXIMUM_SALE_PERCENT = 100;
    private static final int MINIMUM_SALE_PERCENT = 1;
    private static final int UN_APPLIED_SALE_VALUE = 0;

    private Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private boolean isOnSale;
    private final Policy policy;

    public Product(final String name, final int price, final String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isOnSale = false;
        this.policy = new PolicyPercentage(0);
    }

    public Product(final Long id, final String name, final int price, final String imageUrl, final boolean isOnSale, final int amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isOnSale = isOnSale;
        this.policy = new PolicyPercentage(amount);
    }

    public int applySale(int percentage) {
        if (validateRangeOfSalePercentage(percentage)) {
            throw new SalePercentageInvalidRangeException(percentage);
        }

        this.isOnSale = true;
        return policy.updateDiscountValue(percentage);
    }

    private boolean validateRangeOfSalePercentage(final int value) {
        return value > MAXIMUM_SALE_PERCENT || value < MINIMUM_SALE_PERCENT;
    }

    public void unApplySale() {
        policy.updateDiscountValue(UN_APPLIED_SALE_VALUE);
        this.isOnSale = false;
    }

    public int getAppliedDiscountOrOriginPrice() {
        if (!isOnSale) {
            return price;
        }

        return policy.calculate(this.price);
    }

    public int getSalePrice() {
        if (!isOnSale) {
            return 0;
        }

        return price - policy.calculate(price);
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

    public boolean isOnSale() {
        return isOnSale;
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
