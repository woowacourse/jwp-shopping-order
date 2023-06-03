package cart.domain;

import cart.exception.payment.PaymentException;
import cart.exception.point.HavingPointIsLessThanStandardPointException;
import cart.exception.point.HavingPointIsLessThanUsePointException;
import cart.exception.point.UsePointIsLessThanStandardPointException;

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
            throw new HavingPointIsLessThanStandardPointException(
                    member.getPoint().getValue(),
                    MINIMUM_POINT_USAGE_STANDARD.getValue());
        }
        if (usePoint.isSmallerThan(MINIMUM_POINT_USAGE_STANDARD)) {
            throw new UsePointIsLessThanStandardPointException(
                    usePoint.getValue(),
                    MINIMUM_POINT_USAGE_STANDARD.getValue());
        }
    }
}
