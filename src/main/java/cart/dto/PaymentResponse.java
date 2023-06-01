package cart.dto;

import cart.domain.DiscountPolicy;
import cart.domain.Money;
import cart.domain.PaymentRecord;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PaymentResponse {
    private final int originalPrice;
    private final List<DiscountResponse> discounts;
    private final int discountedPrice;
    private final int deliveryFee;
    private final int finalPrice;

    public PaymentResponse(int originalPrice, List<DiscountResponse> discounts, int discountedPrice,
                           int deliveryFee, int finalPrice) {
        this.originalPrice = originalPrice;
        this.discounts = discounts;
        this.discountedPrice = discountedPrice;
        this.deliveryFee = deliveryFee;
        this.finalPrice = finalPrice;
    }

    public static PaymentResponse of(PaymentRecord paymentRecord) {
        Map<DiscountPolicy, Money> policyToDiscountAmounts = paymentRecord.getPolicyToDiscountAmounts();
        List<DiscountResponse> discounts = policyToDiscountAmounts.entrySet().stream()
                .map(entry -> DiscountResponse.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        Money originalTotalPrice = paymentRecord.getOriginalTotalPrice();
        Money discountedAmount = paymentRecord.calculateDiscountedPrice();
        Money deliveryFee = paymentRecord.calculateDeliveryFee();
        Money finalPrice = paymentRecord.calculateFinalPrice();
        return new PaymentResponse(originalTotalPrice.getValue(), discounts, discountedAmount.getValue(),
                deliveryFee.getValue(), finalPrice.getValue());
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public List<DiscountResponse> getDiscounts() {
        return discounts;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public int getFinalPrice() {
        return finalPrice;
    }
}

