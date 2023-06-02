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
        validateTotalPrice();
        validateTotalDiscountPrice();
        validateShippingFee();
        this.totalItemDiscountAmount = totalItemDiscountAmount;
        this.totalMemberDiscountAmount = totalMemberDiscountAmount;
        this.totalItemPrice = totalItemPrice;
        this.discountedTotalItemPrice = discountedTotalItemPrice;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
    }



    private void validateTotalPrice() {
        if (!totalPrice.equals(discountedTotalItemPrice.plus(shippingFee))) {
            throw new IllegalArgumentException("최종 금액이 올바르지 않습니다.");
        }
    }

    private void validateTotalDiscountPrice() {
        if (!totalItemPrice.minus(discountedTotalItemPrice)
                .equals(totalMemberDiscountAmount.plus(totalItemDiscountAmount))) {
            throw new IllegalArgumentException("할인 금액 정보가 올바르지 않습니다.");
        }
    }

    private void validateShippingFee() {
        if ((discountedTotalItemPrice.isOver(MINIMUM_FREE_SHIPPING_STANDARD) && shippingFee.equals(SHIPPING_FEE))
                || discountedTotalItemPrice.isLower(MINIMUM_FREE_SHIPPING_STANDARD) && shippingFee.equals(FREE)) {
            throw new IllegalArgumentException("배송비 정보가 올바르지 않습니다.");
        }
    }
}
