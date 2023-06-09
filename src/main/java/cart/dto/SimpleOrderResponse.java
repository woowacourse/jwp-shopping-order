package cart.dto;

import cart.domain.Order;
import cart.domain.OrderItem;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.List;

public class SimpleOrderResponse {

    private final long id;
    private final String mainProductName;
    private final String mainProductImage;
    private final int extraProductCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private final Timestamp date;
    private final long paymentAmount;

    @JsonCreator
    public SimpleOrderResponse(
            @JsonProperty("id") long id,
            @JsonProperty("mainProductName") String mainProductName,
            @JsonProperty("mainProductImage") String mainProductImage,
            @JsonProperty("extraProductCount") int extraProductCount,
            @JsonProperty("date") Timestamp date,
            @JsonProperty("paymentAmount") long paymentAmount
    ) {
        this.id = id;
        this.mainProductName = mainProductName;
        this.mainProductImage = mainProductImage;
        this.extraProductCount = extraProductCount;
        this.date = date;
        this.paymentAmount = paymentAmount;
    }


    public static SimpleOrderResponse from(Order order) {
        final List<OrderItem> orderItems = order.getOrderItems();
        final OrderItem mainOrderItem = orderItems.get(0);

        return new SimpleOrderResponse(
                order.getId(),
                mainOrderItem.getName(),
                mainOrderItem.getImageUrl(),
                orderItems.size() - 1,
                order.getOrderTime(),
                order.getPriceAfterDiscount()
        );
    }

    public long getId() {
        return id;
    }

    public String getMainProductName() {
        return mainProductName;
    }

    public String getMainProductImage() {
        return mainProductImage;
    }

    public int getExtraProductCount() {
        return extraProductCount;
    }

    public Timestamp getDate() {
        return date;
    }

    public long getPaymentAmount() {
        return paymentAmount;
    }
}
