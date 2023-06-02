package cart.domain.discountpolicy;

import org.springframework.stereotype.Component;

@Component
public class PointPolicy {

    private final double earnedRate = 0.02;
    public int calculateEarnedPoint(int totalPrice) {
        return (int) (totalPrice * earnedRate);
    }
}
