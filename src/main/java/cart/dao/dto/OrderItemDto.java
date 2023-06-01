package cart.dao.dto;

import java.util.List;

public class OrderItemDto {

    private final Long id;
    private final Long orderedProductId;
    private final String orderedProductName;
    private final Integer orderedProductPrice;
    private final String orderedProductImageUrl;
    private final int quantity;
    private final List<Long> memberCouponIds;

    public OrderItemDto(Long id, Long orderedProductId, String orderedProductName, Integer orderedProductPrice,
            String orderedProductImageUrl, int quantity, List<Long> memberCouponIds) {
        this.id = id;
        this.orderedProductId = orderedProductId;
        this.orderedProductName = orderedProductName;
        this.orderedProductPrice = orderedProductPrice;
        this.orderedProductImageUrl = orderedProductImageUrl;
        this.quantity = quantity;
        this.memberCouponIds = memberCouponIds;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderedProductId() {
        return orderedProductId;
    }

    public String getOrderedProductName() {
        return orderedProductName;
    }

    public Integer getOrderedProductPrice() {
        return orderedProductPrice;
    }

    public String getOrderedProductImageUrl() {
        return orderedProductImageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<Long> getMemberCouponIds() {
        return memberCouponIds;
    }
}
