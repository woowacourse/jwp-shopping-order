package cart.domain;

import java.util.ArrayList;
import java.util.List;

public class OrderItems {
    
    private final List<OrderItem> orderItems;
    
    public OrderItems(List<OrderItem> orderItems) {
        this.orderItems = new ArrayList<>(orderItems);
    }
    
    private void validate(OrderItem orderItem) {
        if(orderItem == null) {
            throw new IllegalArgumentException("주문 상품이 입력되지 않았습니다.");
        }
        if(orderItems.contains(orderItem)) {
            throw new IllegalArgumentException("이미 주문목록에 상품이 존재합니다.");
        }
    }
    
    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::calculatePrice)
                .sum();
    }
    
    public List<OrderItem> getOrderItems() {
        return new ArrayList<>(orderItems);
    }
    
}
