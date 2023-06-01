package cart.dto;

import cart.domain.Item;
import cart.domain.Order;
import cart.domain.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final Long id;
    private final String orderNumber;
    private final LocalDate date;
    private final int deliveryFee;
    private final BigDecimal totalOrderPrice;
    private final List<OrderProductResponse> products;

    public OrderResponse(Long id,
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

    public static OrderResponse from(Order order) {
        BigDecimal totalPrice = order.calculateTotalPrice().getValue();
        List<OrderProductResponse> orderProducts = order.getItems().stream()
                .map(OrderProductResponse::from)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getOrderNumber(), order.getOrderDate().toLocalDate(),
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

    static class OrderProductResponse {
        private final Long id;
        private final BigDecimal price;
        private final String name;
        private final String imageUrl;
        private final int quantity;

        OrderProductResponse(Long id, BigDecimal price, String name, String imageUrl, int quantity) {
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
