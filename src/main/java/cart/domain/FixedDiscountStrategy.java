package cart.domain;

import static cart.domain.FixedDiscountStrategy.FixedDiscountStandard.FIRST_STANDARD;
import static cart.domain.FixedDiscountStrategy.FixedDiscountStandard.SECOND_STANDARD;
import static cart.domain.FixedDiscountStrategy.FixedDiscountStandard.THIRD_STANDARD;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class FixedDiscountStrategy implements DiscountStrategy {

    public Payment calculate(Price price) {
        if (price.lessThan(FIRST_STANDARD.getThreshold())) {
            return new Payment(price);
        }
        if (price.lessThan(SECOND_STANDARD.getThreshold())) {
            return new Payment(price, FIRST_STANDARD.getDiscountPrice());
        }
        if (price.lessThan(THIRD_STANDARD.getThreshold())) {
            return new Payment(price, SECOND_STANDARD.getDiscountPrice());
        }
        return new Payment(price, THIRD_STANDARD.getDiscountPrice());
    }

    public Map<Price, Price> getStandards() {
        return FixedDiscountStandard.getAllStandard();
    }


    enum FixedDiscountStandard {
        FIRST_STANDARD(new Price(50_000), new Price(2_000)),
        SECOND_STANDARD(new Price(100_000), new Price(5_000)),
        THIRD_STANDARD(new Price(200_000), new Price(12_000));

        private final Price threshold;
        private final Price discountPrice;

        FixedDiscountStandard(final Price threshold, final Price discountPrice) {
            this.threshold = threshold;
            this.discountPrice = discountPrice;
        }

        Price getThreshold() {
            return threshold;
        }

        Price getDiscountPrice() {
            return discountPrice;
        }

        static Map<Price, Price> getAllStandard() {
            Map<Price, Price> standards = new HashMap<>();
            for (FixedDiscountStandard value : FixedDiscountStandard.values()) {
                standards.put(value.threshold, value.discountPrice);
            }
            return standards;
        }
    }
}
