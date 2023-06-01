package cart.dto.apidatamapper;

import cart.domain.discountpolicy.DiscountType;

import java.util.EnumMap;
import java.util.function.Function;

public class DiscountAmountMapper {

    private static final EnumMap<DiscountType, Function<Double, Integer>> DOMAIN_TO_API_BODY_FUNCTIONS = new EnumMap<>(DiscountType.class);
    private static final EnumMap<DiscountType, Function<Integer, Double>> API_BODY_TO_DOMAIN_FUNCTIONS = new EnumMap<>(DiscountType.class);

    private static final int PERCENT_MULTIPLE_VALUE = 100;
    private static final int HUNDRED_PERCENT_RATE = 1;

    static {
        DOMAIN_TO_API_BODY_FUNCTIONS.put(DiscountType.AMOUNT, Double::intValue);
        DOMAIN_TO_API_BODY_FUNCTIONS.put(DiscountType.RATE, discountValue -> PERCENT_MULTIPLE_VALUE - ((int) (discountValue * PERCENT_MULTIPLE_VALUE)));

        API_BODY_TO_DOMAIN_FUNCTIONS.put(DiscountType.AMOUNT, Integer::doubleValue);
        API_BODY_TO_DOMAIN_FUNCTIONS.put(DiscountType.RATE, discountAmount -> HUNDRED_PERCENT_RATE - (discountAmount / ((double) PERCENT_MULTIPLE_VALUE)));
    }

    public static int domainValueToApiBodyAmount(DiscountType discountType, double value) {
        return DOMAIN_TO_API_BODY_FUNCTIONS.get(discountType)
                .apply(value);
    }

    public static double apiBodyAmountToDomainValue(DiscountType discountType, int amount) {
        return API_BODY_TO_DOMAIN_FUNCTIONS.get(discountType)
                .apply(amount);
    }
}
