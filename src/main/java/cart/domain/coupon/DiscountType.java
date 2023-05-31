package cart.domain.coupon;

import java.util.Arrays;

public enum DiscountType {
    PERCENTAGE("percentage"),
    DEDUCTION("deduction");

    private final String name;

    DiscountType(String name) {
        this.name = name;
    }

    public static DiscountType findByName(String name) {
        return Arrays.stream(DiscountType.values())
                .filter(discountType -> discountType.isSameName(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("요청한 할인 타입이 존재하지 않습니다."));
    }

    public String getName() {
        return name;
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }
}
