package cart.domain;

public class OrderedItem {

    private Long id;
    private Long ordersId;
    private String productName;
    private int productPrice;
    private String productImage;
    private int productQuantity;

    public OrderedItem(Long id, Long ordersId, String productName, int productPrice, String productImage, int productQuantity) {
        this.id = id;
        this.ordersId = ordersId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productQuantity = productQuantity;
    }

    public OrderedItem(Long ordersId, String productName, int productPrice, String productImage, int productQuantity) {
        this.ordersId = ordersId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productQuantity = productQuantity;
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

    public String getProductImage() {
        return productImage;
    }

    public int getProductQuantity() {
        return productQuantity;
    }
}
