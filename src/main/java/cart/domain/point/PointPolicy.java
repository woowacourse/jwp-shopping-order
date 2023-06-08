package cart.domain.point;

import cart.domain.member.Member;

import java.sql.Timestamp;

public interface PointPolicy {

    long calculateEarnedPoint(Member member, long price);

    Timestamp calculateExpiredAt(Timestamp createdAt);

    double getEarningRate(Member member);
}