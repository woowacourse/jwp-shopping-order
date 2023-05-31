package cart.domain;

public class OrderInfo {
    private final Long id;
    private final Product product;
    private final String productName;
    private final Long productPrice;
    private final String productImageUrl;
    private final Long quantity;
    
    public OrderInfo(
            final Product product,
            final String productName,
            final Long productPrice,
            final String productImageUrl,
            final Long quantity
    ) {
        this(null, product, productName, productPrice, productImageUrl, quantity);
    }
    
    public OrderInfo(
            final Long id,
            final Product product,
            final String productName,
            final Long productPrice,
            final String productImageUrl,
            final Long quantity
    ) {
        this.id = id;
        this.product = product;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
    }
    
    public Long calculateProductPriceWithQuantity() {
        return productPrice * quantity;
    }
    
    public Long calculatePointToAdd() {
        return product.calculatePointToAdd() * quantity;
    }
    
    public Long calculateAvailablePoint() {
        return product.calculateAvailablePoint() * quantity;
    }
    
    public Long getId() {
        return id;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public Long getProductPrice() {
        return productPrice;
    }
    
    public String getProductImageUrl() {
        return productImageUrl;
    }
    
    public Long getQuantity() {
        return quantity;
    }
}
