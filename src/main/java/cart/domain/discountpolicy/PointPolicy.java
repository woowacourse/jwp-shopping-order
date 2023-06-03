package cart.domain.discountpolicy;

import org.springframework.stereotype.Component;

@Component
public class PointPolicy {

    private static final double EARNEDRATE = 0.01;
    public int calculateEarnedPoint(int totalPrice) {
        return (int) (totalPrice * EARNEDRATE);
    }
}
