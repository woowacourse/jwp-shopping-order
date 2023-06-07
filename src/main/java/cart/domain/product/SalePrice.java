package cart.domain.product;

import cart.exception.SalePercentageInvalidRangeException;

public class SalePrice {

    private static final int NONE_SALE_PERCENTAGE = 0;
    private static final int MINIMUM_SALE_PERCENT = 1;
    private static final int MAXIMUM_SALE_PERCENT = 100;

    private int salePrice;

    private SalePrice(final int salePrice) {
        this.salePrice = salePrice;
    }

    public static SalePrice createDefault() {
        return new SalePrice(NONE_SALE_PERCENTAGE);
    }

    public static SalePrice from(final Integer salePrice) {
        if (salePrice == null) {
            return createDefault();
        }

        return new SalePrice(salePrice);
    }

    public int applySale(final int price, final int percentage) {
        if (validateRangeOfSalePercentage(percentage)) {
            throw new SalePercentageInvalidRangeException(percentage);
        }

        salePrice = (int) (price * percentage * 0.01);
        return salePrice;
    }

    private boolean validateRangeOfSalePercentage(final int salePercentage) {
        return salePercentage > MAXIMUM_SALE_PERCENT || salePercentage < MINIMUM_SALE_PERCENT;
    }

    public void unApplySale() {
        this.salePrice = NONE_SALE_PERCENTAGE;
    }

    public boolean isNoneSale() {
        return this.salePrice == NONE_SALE_PERCENTAGE;
    }

    public int getSalePrice() {
        return salePrice;
    }
}
