package cart.application.dto;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;

import cart.domain.Order;
import cart.domain.Product;
import cart.domain.QuantityAndProduct;

public class OrderContents {

    private final Long orderId;
    private final Integer payAmount;
    private final LocalDateTime orderAt;
    private final String orderStatus;
    private final String productName;
    private final String productImageUrl;
    private final Integer totalProductCount;

    public OrderContents(Long orderId, Integer payAmount, LocalDateTime orderAt, String orderStatus, String productName,
        String productImageUrl,
        Integer totalProductCount) {
        this.orderId = orderId;
        this.payAmount = payAmount;
        this.orderAt = orderAt;
        this.orderStatus = orderStatus;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.totalProductCount = totalProductCount;
    }

    public static List<OrderContents> from(List<Order> orders) {
        return orders.stream()
            .map(order -> {
                List<QuantityAndProduct> quantityAndProducts = order.getQuantityAndProducts();
                int totalProductCount = (int) quantityAndProducts.stream()
                    .mapToInt(QuantityAndProduct::getQuantity)
                    .count();
                Product representative = quantityAndProducts.get(0).getProduct();
                return new OrderContents(order.getOrderId(), order.getPayAmount(), order.getOrderAt(),
                    order.getOrderStatus().getDisplayName(), representative.getName(), representative.getImageUrl(), totalProductCount);
            })
            .collect(toList());
    }

    public Long getOrderId() {
        return orderId;
    }

    public Integer getPayAmount() {
        return payAmount;
    }

    public LocalDateTime getOrderAt() {
        return orderAt;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public Integer getTotalProductCount() {
        return totalProductCount;
    }
}
