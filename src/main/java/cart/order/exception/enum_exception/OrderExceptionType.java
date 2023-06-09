package cart.order.exception.enum_exception;

import cart.exception.enum_exception.CustomExceptionType;
import org.springframework.http.HttpStatus;

public enum OrderExceptionType implements CustomExceptionType {

  CAN_NOT_CHANGE_NOT_MY_ORDER(
      "사용자의 주문 목록 이외는 수정할 수 없습니다.",
      HttpStatus.BAD_REQUEST
  ),

  CAN_NOT_DISCOUNT_PRICE_MORE_THEN_TOTAL_PRICE(
      "쿠폰 가격이 전체 가격보다 높으면 사용할 수 있습니다.",
      HttpStatus.BAD_REQUEST
  ),

  CAN_NOT_ORDER_NOT_IN_CART(
      "장바구니에 담지 않은 물품은 주문할 수 없습니다.",
      HttpStatus.BAD_REQUEST
  ),

  CAN_NOT_SEARCH_NOT_MY_ORDER(
      "사용자의 주문 목록 이외는 조회할 수 없습니다.",
      HttpStatus.BAD_REQUEST
  ),

  CAN_NOT_FOUND_ORDER(
      "해당 주문은 존재하지 않습니다.",
      HttpStatus.BAD_REQUEST
  ),

  NOT_SAME_TOTAL_PRICE(
      "주문된 총액이 올바르지 않습니다.",
      HttpStatus.BAD_REQUEST
  );

  private final String message;
  private final HttpStatus httpStatus;

  OrderExceptionType(final String message, final HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }

  @Override
  public HttpStatus httpStatus() {
    return httpStatus;
  }

  @Override
  public String errorMessage() {
    return message;
  }
}
