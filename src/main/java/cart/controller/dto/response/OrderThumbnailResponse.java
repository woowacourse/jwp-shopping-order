package cart.controller.dto.response;

import cart.domain.OrderProduct;

import java.time.LocalDateTime;

public class OrderThumbnailResponse {

    private static final int MAIN_PRODUCT_COUNT = 1;

    private final long id;
    private final String mainProductName;
    private final String mainProductImage;
    private final int extraProductCount;
    private final LocalDateTime date;
    private final int price;

    private OrderThumbnailResponse(final long id, final String mainProductName, final String mainProductImage,
                                  final int extraProductCount, final LocalDateTime date, final int price) {
        this.id = id;
        this.mainProductName = mainProductName;
        this.mainProductImage = mainProductImage;
        this.extraProductCount = extraProductCount;
        this.date = date;
        this.price = price;
    }

    public static OrderThumbnailResponse from(final OrderProduct orderProduct) {
        return new OrderThumbnailResponse(
                orderProduct.getOrderInfo().getId(),
                orderProduct.getMainProduct().getName(),
                orderProduct.getMainProduct().getImageUrl(),
                orderProduct.getProductCount() - MAIN_PRODUCT_COUNT,
                orderProduct.getOrderInfo().getCreatedAt(),
                orderProduct.getOrderInfo().getPaymentAmount()
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

    public LocalDateTime getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }
}
