package cart.dto.apidatamapper;

import cart.domain.discountpolicy.DiscountType;
import cart.exception.WrongDiscountTypeInputException;

import java.util.EnumMap;
import java.util.Map;

public class DiscountTypeMapper {

    private static final EnumMap<DiscountType, String> TYPE_API_BODY_STRING = new EnumMap<>(DiscountType.class);

    static {
        TYPE_API_BODY_STRING.put(DiscountType.RATE, "percent");
        TYPE_API_BODY_STRING.put(DiscountType.AMOUNT, "amount");
    }

    public static String domainToApiBodyString(DiscountType discountType) {
        return TYPE_API_BODY_STRING.get(discountType);
    }

    public static DiscountType apiBodyStringToDomain(String typeString) {
        return TYPE_API_BODY_STRING.entrySet().stream()
                .filter(entry -> entry.getValue().equals(typeString))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new WrongDiscountTypeInputException(typeString));
    }
}
