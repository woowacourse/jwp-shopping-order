package cart.domain;

public class OrderItem {

    private final Long id;
    private final Long memberId;
    private final Product product;
    private final int quantity;
    
    public OrderItem(Long id, Long memberId, Product product, int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.product = product;
        this.quantity = quantity;
        validateQuantity(this.quantity);
    }
    
    //todo : 예외 커스텀만들기
    private void validateQuantity(int quantity) {
        if(quantity <= 0) {
            throw new IllegalArgumentException("주문할 상품의 수량은 1개 이상이어야 합니다.");
        }
    }
    
    public Long getId() {
        return id;
    }
    
    public Long getMemberId() {
        return memberId;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public int calculatePrice() {
        return product.getPrice() * quantity;
    }
}
