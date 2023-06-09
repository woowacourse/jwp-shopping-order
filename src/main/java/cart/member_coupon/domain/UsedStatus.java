package cart.member_coupon.domain;

import java.util.Arrays;

public enum UsedStatus {

  USED("Y"),
  UNUSED("N");

  private final String value;

  UsedStatus(final String value) {
    this.value = value;
  }

  public static UsedStatus mapToUsedStatus(final String value) {
    return Arrays.stream(values())
        .filter(it -> it.value.equals(value))
        .findAny()
        .orElseThrow(() -> new IllegalArgumentException("사용 상태는 Y, N으로만 표시 가능합니다."));
  }

  public UsedStatus opposite() {
    if (this == USED) {
      return UNUSED;
    }
    return USED;
  }

  public String getValue() {
    return value;
  }
}
