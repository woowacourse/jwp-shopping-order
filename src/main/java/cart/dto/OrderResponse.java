package cart.dto;

import cart.domain.Order;
import cart.domain.OrderItem;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final long priceBeforeDiscount;
    private final long priceAfterDiscount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private final Timestamp date;
    private final List<Item> orderItems;

    public OrderResponse(long priceBeforeDiscount, long priceAfterDiscount, Timestamp date, List<Item> orderItems) {
        this.priceBeforeDiscount = priceBeforeDiscount;
        this.priceAfterDiscount = priceAfterDiscount;
        this.date = date;
        this.orderItems = orderItems;
    }

    public static OrderResponse from(Order order) {
        final List<Item> orderItems = order.getOrderItems()
                .stream()
                .map(Item::from)
                .collect(Collectors.toUnmodifiableList());

        return new OrderResponse(
                order.getPriceBeforeDiscount(),
                order.getPriceAfterDiscount(),
                order.getOrderTime(),
                orderItems
        );
    }

    public long getPriceBeforeDiscount() {
        return priceBeforeDiscount;
    }

    public long getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public Timestamp getDate() {
        return date;
    }

    public List<Item> getOrderItems() {
        return orderItems;
    }

    public static class Item {

        private final long id;
        private final String name;
        private final int singleProductPrice;
        private final int quantity;
        private final String imageUrl;

        private Item(long id, String name, int singleProductPrice, int quantity, String imageUrl) {
            this.id = id;
            this.name = name;
            this.singleProductPrice = singleProductPrice;
            this.quantity = quantity;
            this.imageUrl = imageUrl;
        }

        public static Item from(OrderItem orderItem) {
            return new Item(
                    orderItem.getId(),
                    orderItem.getName(),
                    orderItem.getProductPrice(),
                    orderItem.getQuantity(),
                    orderItem.getImageUrl()
            );
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getSingleProductPrice() {
            return singleProductPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}
