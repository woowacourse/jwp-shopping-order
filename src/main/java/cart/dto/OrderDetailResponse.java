package cart.dto;

import cart.domain.Item;
import cart.domain.Order;
import cart.domain.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDetailResponse {

    private Long id;
    private String orderNumber;
    private LocalDate date;
    private int deliveryFee;
    private BigDecimal totalOrderPrice;
    private List<OrderProductResponse> products;

    public OrderDetailResponse() {
    }

    public OrderDetailResponse(Long id,
                               String orderNumber,
                               LocalDate date,
                               int deliveryFee,
                               BigDecimal totalOrderPrice,
                               List<OrderProductResponse> products
    ) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.date = date;
        this.deliveryFee = deliveryFee;
        this.totalOrderPrice = totalOrderPrice;
        this.products = products;
    }

    public static OrderDetailResponse from(Order order) {
        BigDecimal totalPrice = order.calculateTotalPrice().getValue();
        List<OrderProductResponse> orderProducts = order.getItems().stream()
                .map(OrderProductResponse::from)
                .collect(Collectors.toList());
        return new OrderDetailResponse(order.getId(), order.getOrderNumber(), order.getOrderDate().toLocalDate(),
                order.getDeliveryFee().getValue().intValue(), totalPrice, orderProducts);
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

    public static class OrderProductResponse {
        private Long id;
        private BigDecimal price;
        private String name;
        private String imageUrl;
        private int quantity;

        public OrderProductResponse() {
        }

        public OrderProductResponse(Long id, BigDecimal price, String name, String imageUrl, int quantity) {
            this.id = id;
            this.price = price;
            this.name = name;
            this.imageUrl = imageUrl;
            this.quantity = quantity;
        }

        private static OrderProductResponse from(Item item) {
            Product product = item.getProduct();
            return new OrderProductResponse(product.getId(), product.getPrice().getValue(), product.getName(),
                    product.getImageUrl(), item.getQuantity());
        }

        public Long getId() {
            return id;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public String getName() {
            return name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
