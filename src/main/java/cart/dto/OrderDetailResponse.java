package cart.dto;

import cart.domain.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDetailResponse {

    private Long id;
    private String orderNumber;
    private LocalDate date;
    private int deliveryFee;
    private String usingCouponName;
    private BigDecimal discountPrice;
    private BigDecimal beforeDiscountPrice;
    private BigDecimal totalOrderPrice;
    private List<OrderProductResponse> products;

    public OrderDetailResponse() {
    }

    public OrderDetailResponse(
            Long id,
            String orderNumber,
            LocalDate date,
            int deliveryFee,
            String usingCouponName,
            BigDecimal discountPrice,
            BigDecimal beforeDiscountPrice,
            BigDecimal totalOrderPrice,
            List<OrderProductResponse> products
    ) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.date = date;
        this.deliveryFee = deliveryFee;
        this.usingCouponName = usingCouponName;
        this.discountPrice = discountPrice;
        this.beforeDiscountPrice = beforeDiscountPrice;
        this.totalOrderPrice = totalOrderPrice;
        this.products = products;
    }

    public static OrderDetailResponse from(Order order) {
        BigDecimal totalOrderPrice = order.calculateTotalPrice().getValue();
        BigDecimal beforeDiscountPrice = order.calculateBeforeDiscountPrice().getValue();
        BigDecimal discountPrice = order.calculateDiscountPrice().getValue();
        List<OrderProductResponse> orderProducts = order.getItems().stream()
                .map(OrderProductResponse::from)
                .collect(Collectors.toList());

        return new OrderDetailResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getOrderDate().toLocalDate(),
                order.getDeliveryFee().getValue().intValue(),
                order.getCoupon().getName(),
                discountPrice,
                beforeDiscountPrice,
                totalOrderPrice,
                orderProducts
        );
    }

    public Long getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public BigDecimal getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public List<OrderProductResponse> getProducts() {
        return products;
    }

    public String getUsingCouponName() {
        return usingCouponName;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public BigDecimal getBeforeDiscountPrice() {
        return beforeDiscountPrice;
    }
}
