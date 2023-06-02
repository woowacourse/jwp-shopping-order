package cart.repository;

import java.time.LocalDateTime;

public class OrderDto {

    private final Long id;
    private final Long memberId;
    private final String email;
    private final String password;
    private final LocalDateTime orderAt;
    private final Integer payAmount;
    private final String orderStatus;
    private final Integer quantity;
    private final Long productId;
    private final String name;
    private final Integer price;
    private final String imageUrl;

    public OrderDto(Long id, Long memberId, String email, String password, LocalDateTime orderAt, Integer payAmount,
        String orderStatus, Integer quantity, Long productId, String name, Integer price, String imageUrl) {
        this.id = id;
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.orderAt = orderAt;
        this.payAmount = payAmount;
        this.orderStatus = orderStatus;
        this.quantity = quantity;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getOrderAt() {
        return orderAt;
    }

    public Integer getPayAmount() {
        return payAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
