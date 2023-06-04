package cart.order.domain;

import java.util.Arrays;

public enum OrderStatus {

  COMPLETE("결제 완료"),
  CANCEL("결제 취소");

  private final String value;

  OrderStatus(final String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static OrderStatus findOrderStatus(final String value) {
    return Arrays.stream(values())
        .filter(it -> it.value.equals(value))
        .findAny()
        .orElseThrow();

  }
}
