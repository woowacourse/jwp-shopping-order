package cart.dto;

public class OrderItemRequest {
    
    private Long product_id;
    private int quantity;
    
    public OrderItemRequest(Long product_id, int quantity) {
        this.product_id = product_id;
        this.quantity = quantity;
    }
    
    public Long getProduct_id() {
        return product_id;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
}
