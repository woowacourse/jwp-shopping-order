package cart.dto.product;

public class ProductPayResponse {

    private final long productId;
    private final String productName;
    private final int price;
    private final String imgUrl;
    private final boolean isOnSale;
    private final int salePrice;

    public ProductPayResponse(final long productId, final String productName, final int price, final String imgUrl, final boolean isOnSale, final int salePrice) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.imgUrl = imgUrl;
        this.isOnSale = isOnSale;
        this.salePrice = salePrice;
    }

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    public int getSalePrice() {
        return salePrice;
    }
}
