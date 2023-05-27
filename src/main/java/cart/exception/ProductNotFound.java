package cart.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFound extends ApplicationException {

    public ProductNotFound() {
        super("존재하지 않는 상품입니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
