package cart.dao;

import java.util.Objects;

public class OrderProductJoinDto {

    private final Long id;
    private final int discountedAmount;
    private final int deliveryAmount;
    private final int totalAmount;
    private final Long member_id;
    private final String address;
    private final Long productId;
    private final String productName;
    private final int productAmount;
    private final String productImageUrl;

    public OrderProductJoinDto(final Long id, final int discountedAmount, final int deliveryAmount,
        final int totalAmount, final Long member_id,
        final String address, final Long productId, final String productName, final int productAmount,
        final String productImageUrl) {
        this.id = id;
        this.discountedAmount = discountedAmount;
        this.deliveryAmount = deliveryAmount;
        this.totalAmount = totalAmount;
        this.member_id = member_id;
        this.address = address;
        this.productId = productId;
        this.productName = productName;
        this.productAmount = productAmount;
        this.productImageUrl = productImageUrl;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderProductJoinDto that = (OrderProductJoinDto) o;
        return getDiscountedAmount() == that.getDiscountedAmount() && getDeliveryAmount() == that.getDeliveryAmount()
            && getTotalAmount() == that.getTotalAmount() && getProductAmount() == that.getProductAmount()
            && Objects.equals(getId(), that.getId()) && Objects.equals(member_id, that.member_id)
            && Objects.equals(getAddress(), that.getAddress()) && Objects.equals(getProductId(),
            that.getProductId()) && Objects.equals(getProductName(), that.getProductName())
            && Objects.equals(getProductImageUrl(), that.getProductImageUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDiscountedAmount(), getDeliveryAmount(), getTotalAmount(), member_id,
            getAddress(),
            getProductId(), getProductName(), getProductAmount(), getProductImageUrl());
    }

    public Long getId() {
        return id;
    }

    public int getDiscountedAmount() {
        return discountedAmount;
    }

    public int getDeliveryAmount() {
        return deliveryAmount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public String getAddress() {
        return address;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public Long getMemberId() {
        return member_id;
    }
}
