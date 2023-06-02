package cart.domain;

import cart.exception.OrderException;
import org.springframework.stereotype.Component;

@Component
public class OrderCalculator {

    private static final int MID_DISCOUNT_THRESHOLD = 30000;
    private static final int HIGH_DISCOUNT_THRESHOLD = 50000;
    static final int MID_DISCOUNT_AMOUNT = 2000;
    static final int HIGH_DISCOUNT_AMOUNT = 5000;

    public void checkPaymentAmount(Order order, long expected) {
        final long actual = calculatePaymentAmount(order);

        if (actual != expected) {
            throw new OrderException.InvalidAmount("계산된 가격이 일치하지 않습니다. 실제 결제 금액: " + actual + " 전달받은 결제 금액: " + expected);
        }
    }

    public long calculatePaymentAmount(Order order) {
        long pricesSum = order.getPriceBeforeDiscount();
        if (MID_DISCOUNT_THRESHOLD <= pricesSum && pricesSum < HIGH_DISCOUNT_THRESHOLD) {
            return pricesSum - MID_DISCOUNT_AMOUNT;
        }
        if (HIGH_DISCOUNT_THRESHOLD <= pricesSum) {
            return pricesSum - HIGH_DISCOUNT_AMOUNT;
        }
        return pricesSum;
    }
}
