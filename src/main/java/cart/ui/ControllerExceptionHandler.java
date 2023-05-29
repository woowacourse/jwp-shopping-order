package cart.ui;

import cart.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleException(CartItemException.IllegalMember e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleMemberNotFoundException(MemberNotFoundException e) {
        ExceptionResponse response = new ExceptionResponse("존재하지 않는 회원입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleProductNotFoundException(ProductNotFoundException e) {
        ExceptionResponse response = new ExceptionResponse("존재하지 않는 상품입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CartItemDuplicatedException.class)
    public ResponseEntity<ExceptionResponse> handleCartItemDuplicatedException(CartItemDuplicatedException e) {
        ExceptionResponse response = new ExceptionResponse("이미 장바구니에 존재하는 상품입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
