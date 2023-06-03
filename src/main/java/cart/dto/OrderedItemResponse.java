package cart.dto;

public class OrderedItemResponse {

    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private int quantity;
    private int discountRate;
    private int discountedPrice;

    public OrderedItemResponse(Long id, String name, int price, String imageUrl, int quantity, int discountRate, int discountedPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.discountRate = discountRate;
        this.discountedPrice = discountedPrice;
    }

    public OrderedItemResponse(String name, int price, String imageUrl, int quantity, int discountRate, int discountedPrice) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.discountRate = discountRate;
        this.discountedPrice = discountedPrice;
    }

    public OrderedItemResponse() {
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

    public int getDiscountedPrice() {
        return discountedPrice;
    }
}
