package cart.domain;

public class OrderedItem {

    private Long id;
    private Long orderId;
    private String name;
    private int price;
    private String imageUrl;
    private int quantity;
    private int discountRate;

    public OrderedItem(Long id, Long orderId, String name, int price, String imageUrl, int quantity, int discountRate) {
        this.id = id;
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.discountRate = discountRate;
    }

    public OrderedItem(Long orderId, String name, int price, String imageUrl, int quantity, int discountRate) {
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.discountRate = discountRate;
    }

    public int calculateDiscountedPrice(){
        if(discountRate > 0){
            return (discountRate * price / 100 - price) * -1;
        }

        return price;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
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
}
