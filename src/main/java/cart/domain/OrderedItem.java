package cart.domain;

public class OrderedItem {

    private Long id;
    private Long orderId;
    private String name;
    private int orice;
    private String imageUrl;
    private int quantity;
    //private boolean isDiscounted;
    private int discountRate;

    public OrderedItem(Long id, Long orderId, String name, int orice, String imageUrl, int quantity, int discountRate) {
        this.id = id;
        this.orderId = orderId;
        this.name = name;
        this.orice = orice;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        //this.isDiscounted = isDiscounted;
        this.discountRate = discountRate;
    }

    public OrderedItem(Long orderId, String name, int orice, String imageUrl, int quantity, int discountRate) {
        this.orderId = orderId;
        this.name = name;
        this.orice = orice;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        //this.isDiscounted = isDiscounted;
        this.discountRate = discountRate;
    }

    public int calculateDiscountedPrice(){
        if(discountRate > 0){
            return (discountRate * orice / 100 - orice) * -1;
        }

        return orice;
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

    public int getOrice() {
        return orice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

//    public boolean getIsDiscounted() {
//        return isDiscounted;
//    }

    public int getDiscountRate() {
        return discountRate;
    }
}
