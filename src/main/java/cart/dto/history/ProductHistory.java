package cart.dto.history;

public class ProductHistory {

    private final Long id;
    private final String productName;
    private final String imgUrl;
    private final int quantity;
    private int price;

    public ProductHistory(final Long id, final String productName, final String imgUrl, final int quantity, final int price) {
        this.id = id;
        this.productName = productName;
        this.imgUrl = imgUrl;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public void updatePrice(final int price) {
        this.price = price;
    }
}
