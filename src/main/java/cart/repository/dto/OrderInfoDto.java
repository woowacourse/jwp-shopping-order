package cart.repository.dto;

import cart.entity.OrderEntity;

public class OrderInfoDto {

    private final long id;
    private final int originalPrice;
    private final int discountPrice;

    private OrderInfoDto(final long id, final int originalPrice, final int discountPrice) {
        this.id = id;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
    }

    public static OrderInfoDto from(final OrderEntity entity) {
        return new OrderInfoDto(entity.getId(), entity.getOriginalPrice(), entity.getDiscountPrice());
    }

    public long getId() {
        return id;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
