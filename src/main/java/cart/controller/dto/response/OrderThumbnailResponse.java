package cart.controller.dto.response;

import cart.repository.dto.OrderAndMainProductDto;

import java.time.LocalDateTime;

public class OrderThumbnailResponse {

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

    public static OrderThumbnailResponse from(final OrderAndMainProductDto dto) {
        return new OrderThumbnailResponse(
                dto.getOrderInfoDto().getId(),
                dto.getMainProduct().getName(),
                dto.getMainProduct().getImageUrl(),
                dto.getExtraProductCount(),
                dto.getOrderInfoDto().getCreatedAt(),
                dto.getOrderInfoDto().getOriginalPrice() - dto.getOrderInfoDto().getDiscountPrice());
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
