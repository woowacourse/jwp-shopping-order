package cart.application.domain;

import java.util.List;

public class OrderInfos {

    private final List<OrderInfo> values;

    public OrderInfos(List<OrderInfo> values) {
        this.values = values;
    }

    public long calculateAvailablePoint() {
        return values.stream()
                .mapToLong(OrderInfo::calculateAvailablePoint)
                .sum();
    }

    public long calculateEarnedPoint() {
        return values.stream()
                .mapToLong(OrderInfo::calculateEarnedPoint)
                .sum();
    }

    public List<OrderInfo> getValues() {
        return values;
    }
}
