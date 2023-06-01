package cart.exception.notfound;

public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException(Long id) {
        super("해당 상품이 존재하지 않습니다. 요청 상품 ID: " + id);
    }
}
