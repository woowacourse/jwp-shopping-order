package cart.exception;

import cart.dto.ProductRequest;

public class ProductRequestException extends RuntimeException {

    // TODO : ProductRequest 예외 해당 클래스로 옮기기
    public ProductRequestException(String message) {
        super(message);
    }

    public static class InvalidProductRequest extends ProductRequestException {

        public InvalidProductRequest(ProductRequest productRequest) {
            super("Illegal ProductRequest value; productRequestName=" + productRequest.getName() + ", productPrice=" + productRequest.getPrice() + ", productPrice=" + productRequest.getImageUrl());
        }
    }
}
