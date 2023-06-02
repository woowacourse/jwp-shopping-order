package cart.domain.bill;

import cart.domain.value.Money;

public class Bill {

    private static final Money MINIMUM_FREE_SHIPPING_STANDARD = new Money(50_000);
    private static final Money SHIPPING_FEE = new Money(3000);
    private static final Money FREE = new Money(0);

    private final Money totalItemDiscountAmount;
    private final Money totalMemberDiscountAmount;
    private final Money totalItemPrice;
    private final Money discountedTotalItemPrice;
    private final Money shippingFee;
    private final Money totalPrice;

    public Bill(
            final Money totalItemDiscountAmount,
            final Money totalMemberDiscountAmount,
            final Money totalItemPrice,
            final Money discountedTotalItemPrice,
            final Money shippingFee,
            final Money totalPrice
    ) {
        validateTotalPrice(totalPrice, discountedTotalItemPrice, shippingFee);
        validateTotalDiscountPrice(totalItemPrice, discountedTotalItemPrice, totalMemberDiscountAmount, totalItemDiscountAmount );
        validateShippingFee(discountedTotalItemPrice, shippingFee);
        this.totalItemDiscountAmount = totalItemDiscountAmount;
        this.totalMemberDiscountAmount = totalMemberDiscountAmount;
        this.totalItemPrice = totalItemPrice;
        this.discountedTotalItemPrice = discountedTotalItemPrice;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
    }

    public void validateRequest(final int totalItemPrice,
                           final int discountedTotalItemPrice,
                           final int totalItemDiscountAmount,
                           final int totalMemberDiscountAmount,
                           final int shippingFee
    ) {
        if (!checkBill(totalItemPrice, discountedTotalItemPrice, totalItemDiscountAmount, totalMemberDiscountAmount, shippingFee)) {
            throw new IllegalArgumentException("주문 요청 정보가 올바르지 않습니다.");
        }
    }

    private boolean checkBill(final int totalItemPrice,
                              final int discountedTotalItemPrice,
                              final int totalItemDiscountAmount,
                              final int totalMemberDiscountAmount,
                              final int shippingFee
    ) {
        return this.totalItemPrice.equals(new Money(totalItemPrice))
                && this.discountedTotalItemPrice.equals(new Money(discountedTotalItemPrice))
                && this.totalItemDiscountAmount.equals(new Money(totalItemDiscountAmount))
                && this.totalMemberDiscountAmount.equals(new Money(totalMemberDiscountAmount))
                && this.shippingFee.equals(new Money(shippingFee));
    }

    private void validateTotalPrice(final Money totalPrice, final Money discountedTotalItemPrice, final Money shippingFee) {
        if (!totalPrice.equals(discountedTotalItemPrice.plus(shippingFee))) {
            throw new IllegalArgumentException("최종 금액이 올바르지 않습니다.");
        }
    }

    private void validateTotalDiscountPrice(final Money totalItemPrice, final Money discountedTotalItemPrice, final Money totalMemberDiscountAmount, final Money totalItemDiscountAmount) {
        if (!totalItemPrice.minus(discountedTotalItemPrice)
                .equals(totalMemberDiscountAmount.plus(totalItemDiscountAmount))) {
            throw new IllegalArgumentException("할인 금액 정보가 올바르지 않습니다.");
        }
    }

    private void validateShippingFee(final Money discountedTotalItemPrice, final Money shippingFee) {
        if ((discountedTotalItemPrice.isOver(MINIMUM_FREE_SHIPPING_STANDARD) && shippingFee.equals(SHIPPING_FEE))
                || discountedTotalItemPrice.isLower(MINIMUM_FREE_SHIPPING_STANDARD) && shippingFee.equals(FREE)) {
            throw new IllegalArgumentException("배송비 정보가 올바르지 않습니다.");
        }
    }

    public int getTotalItemDiscountAmount() {
        return totalItemDiscountAmount.getMoney();
    }

    public int getTotalMemberDiscountAmount() {
        return totalMemberDiscountAmount.getMoney();
    }

    public int getTotalItemPrice() {
        return totalItemPrice.getMoney();
    }

    public int getDiscountedTotalItemPrice() {
        return discountedTotalItemPrice.getMoney();
    }

    public int getShippingFee() {
        return shippingFee.getMoney();
    }

    public int getTotalPrice() {
        return totalPrice.getMoney();
    }
}
