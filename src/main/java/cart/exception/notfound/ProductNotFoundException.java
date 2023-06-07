package cart.exception.notfound;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException() {
        super("찾을 수 없는 상품입니다.");
    }
}
