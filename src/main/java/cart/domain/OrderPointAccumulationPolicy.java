package cart.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OrderPointAccumulationPolicy implements PointAccumulationPolicy {

    private static final String ORDER_POINT_COMMENT = "상품 구매 포인트";

    private final PointExpirePolicy pointExpirePolicy;

    public OrderPointAccumulationPolicy(PointExpirePolicy pointExpirePolicy) {
        this.pointExpirePolicy = pointExpirePolicy;
    }

    @Override
    public Point calculateAccumulationPoint(int totalCost) {
        if (totalCost < 50000) {
            return Point.of((int) Math.round(totalCost * 0.05), ORDER_POINT_COMMENT, pointExpirePolicy.calculateExpireDate(LocalDate.now()));
        }
        if (totalCost < 100000) {
            return Point.of((int) Math.round(totalCost * 0.08), ORDER_POINT_COMMENT, pointExpirePolicy.calculateExpireDate(LocalDate.now()));
        }
        return Point.of((int) Math.round(totalCost * 0.1), ORDER_POINT_COMMENT, pointExpirePolicy.calculateExpireDate(LocalDate.now()));
    }
}
