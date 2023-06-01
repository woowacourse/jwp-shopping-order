package cart.exception.handler;

import cart.exception.ExceptionResponse;
import cart.exception.customexception.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final String DEFAULT_MESSAGE = "알 수 없는 오류입니다.";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidParamException(final MethodArgumentNotValidException e) {
        final String errorMessage = e.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(errorMessage, 101));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ExceptionResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        ExceptionResponse response = new ExceptionResponse("잘못된 경로입니다.", 102);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handlerAuthenticationException(AuthenticationException e) {
        ExceptionResponse response = new ExceptionResponse("회원 정보가 틀렸습니다.", e.ERROR_CODE);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleMemberNotFoundException(MemberNotFoundException e) {
        ExceptionResponse response = new ExceptionResponse("존재하지 않는 회원입니다.", e.ERROR_CODE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleProductNotFoundException(ProductNotFoundException e) {
        ExceptionResponse response = new ExceptionResponse("존재하지 않는 상품입니다.", e.ERROR_CODE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCartItemNotFoundException(CartItemNotFoundException e) {
        ExceptionResponse response = new ExceptionResponse("장바구니에 아이템이 존재하지 않습니다.", e.ERROR_CODE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CartItemDuplicatedException.class)
    public ResponseEntity<ExceptionResponse> handleCartItemDuplicatedException(CartItemDuplicatedException e) {
        ExceptionResponse response = new ExceptionResponse("이미 장바구니에 존재하는 상품입니다.", e.ERROR_CODE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CartItemQuantityExcessException.class)
    public ResponseEntity<ExceptionResponse> handleCartItemQuantityExcessException(CartItemQuantityExcessException e) {
        ExceptionResponse response = new ExceptionResponse("상품의 재고가 부족합니다.", e.ERROR_CODE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(OrderTotalPriceIsNotMatchedException.class)
    public ResponseEntity<ExceptionResponse> handleOrderTotalPriceIsNotMatchedException(OrderTotalPriceIsNotMatchedException e) {
        ExceptionResponse response = new ExceptionResponse("주문 가격과 상품 가격의 합이 맞지 않습니다.", e.ERROR_CODE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleOrderNotFoundException(OrderNotFoundException e) {
        ExceptionResponse response = new ExceptionResponse("해당 주문을 찾을 수 없습니다.", e.ERROR_CODE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalMemberException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalMemberException(IllegalMemberException e) {
        ExceptionResponse response = new ExceptionResponse("접근할 수 없는 유저입니다.", e.ERROR_CODE);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(PointExceedTotalPriceException.class)
    public ResponseEntity<ExceptionResponse> handleUsePointExceedTotalPriceException(PointExceedTotalPriceException e) {
        ExceptionResponse response = new ExceptionResponse("상품의 총 가격보다 많은 포인트를 사용할 수 없습니다.", e.ERROR_CODE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(PointNotEnoughException.class)
    public ResponseEntity<ExceptionResponse> handlePointNotEnoughException(PointNotEnoughException e) {
        ExceptionResponse response = new ExceptionResponse("포인트가 부족합니다.", e.ERROR_CODE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        ExceptionResponse response = new ExceptionResponse(DEFAULT_MESSAGE, 100);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
