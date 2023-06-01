package cart.domain;

import java.time.LocalDate;

public class OrderPointAccumulationPolicy implements PointAccumulationPolicy {

    private static final String ORDER_POINT_COMMENT = "상품 구매 포인트";

    private final PointExpirePolicy pointExpirePolicy;

    public OrderPointAccumulationPolicy(PointExpirePolicy pointExpirePolicy) {
        this.pointExpirePolicy = pointExpirePolicy;
    }

    @Override
    public Point calculateAccumulationPoint(int totalCost) { // 상수화보단 가독성 있어보임 -> 질문
        if (totalCost < 50000) {
            return Point.of((int) Math.round(totalCost * 0.05), ORDER_POINT_COMMENT, pointExpirePolicy.calculateExpireDate(LocalDate.now()));
        }
        if (totalCost < 100000) {
            return Point.of((int) Math.round(totalCost * 0.08), ORDER_POINT_COMMENT, pointExpirePolicy.calculateExpireDate(LocalDate.now()));
        }
        return Point.of((int) Math.round(totalCost * 0.1), ORDER_POINT_COMMENT, pointExpirePolicy.calculateExpireDate(LocalDate.now()));
    }
}
