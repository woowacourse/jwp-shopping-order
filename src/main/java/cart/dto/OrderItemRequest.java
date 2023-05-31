package cart.dto;

public class OrderItemRequest {
    
    private Long id;
    private int quantity;
    
    public OrderItemRequest(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }
    
    public Long getId() {
        return id;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
}
