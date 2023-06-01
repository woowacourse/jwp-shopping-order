package cart.domain;

public class OrderedItem {

    private Long id;
    private Long ordersId;
    private String productName;
    private int productPrice;
    private String productImageUrl;
    private int productQuantity;
    private boolean isDiscounted;
    private int discountedRate;

    public OrderedItem(Long id, Long ordersId, String productName, int productPrice, String productImageUrl, int productQuantity, boolean isDiscounted, int discountedRate) {
        this.id = id;
        this.ordersId = ordersId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.productQuantity = productQuantity;
        this.isDiscounted = isDiscounted;
        this.discountedRate = discountedRate;
    }

    public OrderedItem(Long ordersId, String productName, int productPrice, String productImageUrl, int productQuantity, boolean isDiscounted, int discountedRate) {
        this.ordersId = ordersId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.productQuantity = productQuantity;
        this.isDiscounted = isDiscounted;
        this.discountedRate = discountedRate;
    }

    public int calculateDiscountedPrice(){
        if(discountedRate > 0){
            return (discountedRate * productPrice / 100 - productPrice) * -1;
        }

        return productPrice;
    }

    public Long getId() {
        return id;
    }

    public Long getOrdersId() {
        return ordersId;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public boolean getIsDiscounted() {
        return isDiscounted;
    }

    public int getDiscountedRate() {
        return discountedRate;
    }
}
