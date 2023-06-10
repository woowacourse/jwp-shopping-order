package cart.domain.coupon;

public interface Strategy {
    int calculate(int price, int amount);

    StrategyName getStrategyName();
}
