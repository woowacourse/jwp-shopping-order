package cart.entity;

public class OrderedItemEntity {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final int quantity;
    private final int discountRate;
    private final Long orderedId;

    public OrderedItemEntity(
            final Long id,
            final String name,
            final int price,
            final String imageUrl,
            final int quantity,
            final int discountRate,
            final Long orderedId
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.discountRate = discountRate;
        this.orderedId = orderedId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public Long getOrderedId() {
        return orderedId;
    }
}
