package cart.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private Long id;
    private Long memberId;
    private int totalPrice;
    private LocalDateTime orderedAt;
    private int shippingFee;
    private int discountedTotalPrice;

    public Order(Long id, Long memberId, int totalPrice, LocalDateTime orderedAt, int shippingFee, int discountedTotalPrice) {
        this.id = id;
        this.memberId = memberId;
        this.totalPrice = totalPrice;
        this.orderedAt = orderedAt;
        this.shippingFee = shippingFee;
        this.discountedTotalPrice = discountedTotalPrice;
    }

    public Order(Long memberId, int totalPrice, LocalDateTime orderedAt, int shippingFee, int discountedTotalPrice) {
        this.id = null;
        this.memberId = memberId;
        this.totalPrice = totalPrice;
        this.orderedAt = orderedAt;
        this.shippingFee = shippingFee;
        this.discountedTotalPrice = discountedTotalPrice;
    }

    public static int calculateTotalPrice(List<Integer> prices){
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

    public int getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public int getDiscountedTotalPrice() {
        return discountedTotalPrice;
    }
}
