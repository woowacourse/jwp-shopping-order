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

    @ExceptionHandler(CartItemException.IllegalMemberException.class)
    public ResponseEntity<Void> handleException(CartItemException.IllegalMemberException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(CartItemException.NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(CartItemException.NotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("해당 장바구니 아이템을 찾을 수 없습니다. 잘못된 요청입니다."));
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

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(OrderNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("해당 주문을 찾을 수 없습니다."));
    }

    @ExceptionHandler(InvalidOrderCalculationException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidOrderCalculationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("주문 금액과 실제 계산예정금액이 불일치합니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("오류가 발생했습니다." + e.getMessage()));
    }

}
