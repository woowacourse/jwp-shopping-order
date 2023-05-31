package cart.domain;

public class OrderInfo {
    private final Long id;
    private final Product product;
    private final String productName;
    private final Integer productPrice;
    private final String productImageUrl;
    private final Integer quantity;
    
    public OrderInfo(
            final Product product,
            final String productName,
            final int productPrice,
            final String productImageUrl,
            final int quantity
    ) {
        this(null, product, productName, productPrice, productImageUrl, quantity);
    }
    
    public OrderInfo(
            final Long id,
            final Product product,
            final String productName,
            final int productPrice,
            final String productImageUrl,
            final int quantity
    ) {
        this.id = id;
        this.product = product;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
    }
    
    public Long calculateProductPriceWithQuantiry() {
        return (long) productPrice * quantity;
    }
    
    public Long calculatePointToAdd() {
        return product.calculatePointToAdd() * quantity;
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
    
    public Integer getProductPrice() {
        return productPrice;
    }
    
    public String getProductImageUrl() {
        return productImageUrl;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
}
