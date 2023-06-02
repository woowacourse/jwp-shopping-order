package cart.ui;

import cart.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleException(CartItemException.IllegalMember e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(CannotApplyCouponException.class)
    public ResponseEntity<ErrorResponse> handleException(CannotApplyCouponException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("쿠폰을 적용할 수 없습니다. 할인가격은 상품가격보다 클 수 없습니다."));
    }

    @ExceptionHandler(CannotPayException.class)
    public ResponseEntity<ErrorResponse> handleException(CannotPayException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("결제할 수 없습니다. 해당 사용자의 자산이 부족합니다."));
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(CouponNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("존재하지 않거나 이미 사용된 쿠폰입니다."));
    }

    @ExceptionHandler(InvalidOrderCalculationException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidOrderCalculationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("주문 금액과 실제 계산예정금액이 불일치합니다."));
    }
}
