package cart.domain.discountpolicy;

import java.util.HashMap;
import java.util.Map;

public enum DiscountType {
    RATE("rateDiscountPolicy", 1),
    AMOUNT("amountDiscountPolicy", 2);

    private static final Map<String, DiscountType> BEAN_NAME_MAP = new HashMap<>();

    static {
        for (DiscountType type : values()) {
            BEAN_NAME_MAP.put(type.getBeanName(), type);
        }
    }

    private final String beanName;
    private final int priority;

    DiscountType(String beanName, int priority) {
        this.beanName = beanName;
        this.priority = priority;
    }

    public static DiscountType of(String beanName) {
        return BEAN_NAME_MAP.computeIfAbsent(beanName, key -> {
            throw new IllegalArgumentException("입력된 이름의 bean이 없습니다. - " + beanName);
        });
    }

    public int getPriority() {
        return priority;
    }

    public String getBeanName() {
        return beanName;
    }
}
