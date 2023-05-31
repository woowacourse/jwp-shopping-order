package cart.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private Long id;
    private Long memberId;
    private LocalDateTime orderedAt;
    private int totalItemDiscountAmount;
    private int totalMemberDiscountAmount;
    private int totalItemPrice;
    private int discountedTotalItemPrice;
    private int shippingFee;
    private int totalPrice;

    public Order(Long id, Long memberId, LocalDateTime orderedAt, int totalItemDiscountAmount, int totalMemberDiscountAmount, int totalItemPrice, int discountedTotalItemPrice, int shippingFee, int totalPrice) {
        this.id = id;
        this.memberId = memberId;
        this.orderedAt = orderedAt;
        this.totalItemDiscountAmount = totalItemDiscountAmount;
        this.totalMemberDiscountAmount = totalMemberDiscountAmount;
        this.totalItemPrice = totalItemPrice;
        this.discountedTotalItemPrice = discountedTotalItemPrice;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
    }

    public Order(Long memberId, LocalDateTime orderedAt, int totalItemDiscountAmount, int totalMemberDiscountAmount, int totalItemPrice, int discountedTotalItemPrice, int shippingFee, int totalPrice) {
        this.memberId = memberId;
        this.orderedAt = orderedAt;
        this.totalItemDiscountAmount = totalItemDiscountAmount;
        this.totalMemberDiscountAmount = totalMemberDiscountAmount;
        this.totalItemPrice = totalItemPrice;
        this.discountedTotalItemPrice = discountedTotalItemPrice;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
    }

    public static int calculatePriceSum(List<Integer> prices){
        return prices.stream()
                .mapToInt(price -> price)
                .sum();
    }

    public static int calculateShippingFee(int totalPrice) {
        if(totalPrice >= 50000){
            return 0;
        }
        return 3000;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public int getTotalItemDiscountAmount() {
        return totalItemDiscountAmount;
    }

    public int getTotalMemberDiscountAmount() {
        return totalMemberDiscountAmount;
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

    public int getTotalPrice() {
        return totalPrice;
    }
}
