package cart.dao;

public class OrderProductJoinDto {

    private final Long id;
    private final int discountAmount;
    private final int deliveryAmount;
    private final int totalAmount;
    private final String address;
    private final Long productId;
    private final String productName;
    private final int productAmount;
    private final String productImageUrl;

    public OrderProductJoinDto(final Long id, final int discountAmount, final int deliveryAmount, final int totalAmount,
        final String address,
        final Long productId, final String productName, final int productAmount, final String productImageUrl) {
        this.id = id;
        this.discountAmount = discountAmount;
        this.deliveryAmount = deliveryAmount;
        this.totalAmount = totalAmount;
        this.address = address;
        this.productId = productId;
        this.productName = productName;
        this.productAmount = productAmount;
        this.productImageUrl = productImageUrl;
    }

    public Long getId() {
        return id;
    }

    public int getDiscountAmount() {
        return discountAmount;
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
}
