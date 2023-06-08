package cart.domain.order;

import cart.domain.member.Member;
import cart.domain.point.Point;
import cart.domain.point.PointPolicy;
import cart.exception.customexception.CartException;
import cart.exception.customexception.ErrorCode;

import java.sql.Timestamp;

public class OrderPoint {

    private final PointPolicy pointPolicy;

    public OrderPoint(PointPolicy pointPolicy) {
        this.pointPolicy = pointPolicy;
    }

    public Point earnPoint(Member member, long usedPoint, long totalPrice, Timestamp createdAt) {
        if (totalPrice < usedPoint) {
            throw new CartException(ErrorCode.POINT_EXCEED_TOTAL_PRICE);
        }
        long earnedPoint = pointPolicy.calculateEarnedPoint(member, totalPrice - usedPoint);
        Timestamp expiredAt = pointPolicy.calculateExpiredAt(createdAt);
        return new Point(earnedPoint, earnedPoint, member, expiredAt, createdAt);
    }
}
