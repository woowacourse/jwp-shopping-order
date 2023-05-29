package cart.dto.product;

import cart.domain.product.Product;

public class ProductPayResponse {

    private final long productId;
    private final String productName;
    private final int price;
    private final int quantity;
    private final String imgUrl;
    private final Boolean isOnSale;
    private final int salePrice;

    private ProductPayResponse(final long productId, final String productName, final int price, final int quantity, final String imgUrl, final Boolean isOnSale, final int salePrice) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imgUrl = imgUrl;
        this.isOnSale = isOnSale;
        this.salePrice = salePrice;
    }

    public static ProductPayResponse from(final Product product, final int quantity) {
        return new ProductPayResponse(product.getId(), product.getName(), product.getPrice(), quantity,  product.getImageUrl(), product.isOnSale(), product.getSalePrice());
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

    public int getQuantity() {
        return quantity;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public boolean getIsOnSale() {
        return isOnSale;
    }

    public int getSalePrice() {
        return salePrice;
    }
}
