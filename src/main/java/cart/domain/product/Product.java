package cart.domain.product;

import cart.domain.discount.Policy;
import cart.domain.discount.PolicyPercentage;

import java.util.Objects;

public class Product {

    private static final int MAXIMUM_SALE_PERCENT = 100;
    private static final int MINIMUM_SALE_PERCENT = 1;

    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private boolean isOnSale;
    private Policy policy;

    public Product(final String name, final int price, final String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isOnSale = false;
        this.policy = new PolicyPercentage(0);
    }

    public Product(final Long id, final String name, final int price, final String imageUrl, final boolean isOnSale ,final int amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isOnSale = isOnSale;
        this.policy = new PolicyPercentage(amount);
    }

    public void applySale(int value) {
        if (value > MAXIMUM_SALE_PERCENT || value < MINIMUM_SALE_PERCENT) {
            throw new IllegalArgumentException("상품 세일은 최대 1~100%까지 가능합니다.");
        }

        policy.updateDiscountValue(value);
        this.isOnSale = true;
    }

    public void unApplySale() {
        policy.updateDiscountValue(0);
        this.isOnSale = false;
    }

    public int getApplyDiscountPrice() {
        validateIsOnSale();
        return policy.calculate(this.price);
    }

    public int getSalePrice() {
        if (!isOnSale) {
            return 0;
        }

        return price - policy.calculate(price);
    }

    private void validateIsOnSale() {
        if (!isOnSale) {
            throw new IllegalArgumentException("세일 적용 중이 아닙니다.");
        }
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
