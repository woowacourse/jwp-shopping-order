package cart.order.exception.enum_exception;

import cart.exception.enum_exception.CustomExceptionType;
import org.springframework.http.HttpStatus;

public enum OrderExceptionType implements CustomExceptionType {

  CAN_NOT_CHANGE_NOT_MY_ORDER(
      "사용자의 주문 목록 이외는 수정할 수 없습니다.",
      HttpStatus.BAD_REQUEST
  ),

  CAN_NOT_SEARCH_NOT_MY_ORDER(
      "사용자의 주문 목록 이외는 조회할 수 없습니다.",
      HttpStatus.BAD_REQUEST
  ),

  CAN_NOT_FOUND_ORDER(
      "해당 주문은 존재하지 않습니다.",
      HttpStatus.BAD_REQUEST
  )
  ;

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
