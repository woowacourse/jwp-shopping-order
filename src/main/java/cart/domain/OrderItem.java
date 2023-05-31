package cart.domain;

import java.util.Objects;

public class OrderItem {
    
    private final Long id;
    private final Product product;
    private final int quantity;
    
    public OrderItem(Long id, Product product, int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        validateQuantity(this.quantity);
    }
    
    //todo : 예외 커스텀만들기
    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("주문할 상품의 수량은 1개 이상이어야 합니다.");
        }
    }
    
    public Long getId() {
        return id;
    }
    
    public int calculatePrice() {
        return product.getPrice() * quantity;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    public Product getProduct() {
        return product;
    }
    
    public int getQuantity() {
        return quantity;
    }
}
