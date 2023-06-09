package cart.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private Long id;
    private int totalItemPrice;
    private int discountedTotalItemPrice;
    private int shippingFee;
    private LocalDateTime orderedAt;
    private Long memberId;

    public Order(Long id, int totalItemPrice, int discountedTotalItemPrice, int shippingFee, LocalDateTime orderedAt, Long memberId) {
        this.id = id;
        this.totalItemPrice = totalItemPrice;
        this.discountedTotalItemPrice = discountedTotalItemPrice;
        this.shippingFee = shippingFee;
        this.orderedAt = orderedAt;
        this.memberId = memberId;
    }

    public Order(int totalItemPrice, int discountedTotalItemPrice, int shippingFee, Long memberId) {
        this.totalItemPrice = totalItemPrice;
        this.discountedTotalItemPrice = discountedTotalItemPrice;
        this.shippingFee = shippingFee;
        this.memberId = memberId;
    }

    public static int calculatePriceSum(List<Integer> prices){
        return prices.stream()
                .mapToInt(price -> price)
                .sum();
    }

    public Long getId() {
        return id;
    }

    public int getTotalItemPrice() {
        return totalItemPrice;
    }

    public int getDiscountedTotalItemPrice() {
        return discountedTotalItemPrice;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public Long getMemberId() {
        return memberId;
    }
}
