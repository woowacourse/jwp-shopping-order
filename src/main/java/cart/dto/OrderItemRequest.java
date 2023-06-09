package cart.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderItemRequest {
    
    @NotNull
    private Long id;
    @NotNull
    @Min(value = 1, message = "주문할 상품의 수량은 1개 이상이어야 합니다.")
    private Integer quantity;
    
    public OrderItemRequest(Long id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }
    
    public Long getId() {
        return id;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
}
