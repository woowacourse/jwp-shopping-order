package cart.domain;

import cart.exception.payment.PaymentException;

public class PointPolicy {
    public static final Point MINIMUM_POINT_USAGE_STANDARD = new Point(3000);
    public static final double POINT_EARNING_RATE = 0.05;

    public static void usePoint(Member member, Point usePoint) {
        if (usePoint.isZero()) {
            return;
        }
        judgeMemberCanUsePoint(member, usePoint);
        member.usePoint(usePoint);
    }

    public static void earnPoint(Member member, int totalProductPrice) {
        Point earningPoint = new Point((int) (totalProductPrice * POINT_EARNING_RATE));
        member.earnPoint(earningPoint);
    }

    private static void judgeMemberCanUsePoint(Member member, Point usePoint) {
        if (member.getPoint().isSmallerThan(MINIMUM_POINT_USAGE_STANDARD)) {
            throw new PaymentException("보유한 포인트가 사용할 수 있는 최소 포인트보다 적습니다."
                    + "보유 포인트 : " + Integer.toString(member.getPoint().getValue())
                    + "사용할 수 있는 최소 포인트 : " + Integer.toString(MINIMUM_POINT_USAGE_STANDARD.getValue()));
        }
        if (usePoint.isSmallerThan(MINIMUM_POINT_USAGE_STANDARD)) {
            throw new PaymentException("사용하고자 하는 포인트가 사용할 수 있는 최소 포인트보다 적습니다."
                    + "사용하고자 하는 포인트 : " + Integer.toString(usePoint.getValue())
                    + "사용할 수 있는 최소 포인트 : " + Integer.toString(MINIMUM_POINT_USAGE_STANDARD.getValue()));
        }
    }
}
