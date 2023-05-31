package cart.domain;

public class PointPolicy {
    public static final Point MINIMUM_POINT_USAGE_STANDARD = new Point(3000);

    public static void usePoint(Member member, Point usePoint) {
        judgeMemberCanUsePoint(member, usePoint);
        member.usePoint(usePoint);
    }

    private static void judgeMemberCanUsePoint(Member member, Point usePoint) {
        if (member.getPoint().isSmallerThan(MINIMUM_POINT_USAGE_STANDARD)) {
            throw new IllegalArgumentException("보유한 포인트가 사용할 수 있는 최소 포인트보다 적습니다."
                    + "보유 포인트 : "+Integer.toString(member.getPoint().getValue())
                    + "사용할 수 있는 최소 포인트 : "+Integer.toString(MINIMUM_POINT_USAGE_STANDARD.getValue()));
        }
        if (usePoint.isSmallerThan(MINIMUM_POINT_USAGE_STANDARD)) {
            throw new IllegalArgumentException("사용하고자 하는 포인트가 사용할 수 있는 최소 포인트보다 적습니다."
                    + "사용하고자 하는 포인트 : "+Integer.toString(usePoint.getValue())
                    + "사용할 수 있는 최소 포인트 : "+Integer.toString(MINIMUM_POINT_USAGE_STANDARD.getValue()));
        }
    }
}
