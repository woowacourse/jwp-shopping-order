package cart.domain;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cart.exception.IllegalPointException;

public class Points {

    private final List<PointAddition> pointAdditions;
    private final List<PointUsage> pointUsages;

    public Points(List<PointAddition> pointAdditions, List<PointUsage> pointUsages) {
        this.pointAdditions = pointAdditions;
        this.pointUsages = pointUsages;
    }

    public boolean hasNotEnoughPoint(int usePointAmount) {
        int remainingPoint = getTotalRemainingPoint();
        return remainingPoint < usePointAmount;
    }

    public int getTotalRemainingPoint() {
        int totalAddition = pointAdditions.stream()
            .mapToInt(PointAddition::getAmount)
            .sum();
        int totalUsage = pointUsages.stream()
            .mapToInt(PointUsage::getAmount)
            .sum();
        return totalAddition - totalUsage;
    }

    public List<PointUsage> findPointsToBeUsed(int usePointAmount) {
        List<PointAddition> unusedPointsAscendingByExpireAt = findUnusedPointsAscendingByExpireAt();

        return getPointsToBeUsed(usePointAmount, unusedPointsAscendingByExpireAt);
    }

    private List<PointAddition> findUnusedPointsAscendingByExpireAt() {
        Map<Long, List<PointUsage>> usagesByPointAdditionId = pointUsages.stream()
            .collect(groupingBy(PointUsage::getPointAdditionId));
        List<PointAddition> unusedPoints = new ArrayList<>();
        List<PointAddition> partiallyUsedPoints = new ArrayList<>();
        for (PointAddition addition : pointAdditions) {
            if (!usagesByPointAdditionId.containsKey(addition.getId())) {
                unusedPoints.add(addition);
                continue;
            }
            List<PointUsage> usages = usagesByPointAdditionId.get(addition.getId());
            int amount = usages.stream()
                .mapToInt(PointUsage::getAmount)
                .sum();
            if (addition.getAmount() != amount) {
                partiallyUsedPoints.add(addition.reduce(amount));
            }
        }
        assertContainingOnePartiallyUsedPointAtMax(partiallyUsedPoints);
        unusedPoints.addAll(partiallyUsedPoints);
        return unusedPoints.stream()
            .sorted(comparing(PointAddition::getExpireAt))
            .collect(toList());
    }

    private void assertContainingOnePartiallyUsedPointAtMax(List<PointAddition> partiallyUsedPoints) {
        if (partiallyUsedPoints.size() > 1) {
            throw new IllegalPointException("부분적으로 사용된 포인트가 한 개 이상입니다");
        }
    }

    private List<PointUsage> getPointsToBeUsed(int usePointAmount, List<PointAddition> sortedPoints) {
        int pointSum = 0;
        int i = 0;
        while (pointSum < usePointAmount) {
            pointSum += sortedPoints.get(i).getAmount();
            i++;
        }
        int remainder = pointSum - usePointAmount;
        if (remainder == 0) {
            return getPointsToBeUsedWithoutRemainder(sortedPoints, i);
        }
        return getPointsToBeUsedWithRemainder(sortedPoints, i, remainder);
    }

    private List<PointUsage> getPointsToBeUsedWithoutRemainder(List<PointAddition> unusedPointsDescendingByCreatedAt,
        int i) {
        List<PointAddition> pointsToBeUsed = unusedPointsDescendingByCreatedAt.subList(0, i);
        return pointsToBeUsed.stream()
            .map(pointAddition -> PointUsage.from(pointAddition.getMemberId(), pointAddition.getOrderId(),
                pointAddition.getId(), pointAddition.getAmount()))
            .collect(toList());
    }

    private List<PointUsage> getPointsToBeUsedWithRemainder(List<PointAddition> unusedPointsDescendingByCreatedAt,
        int i, int remainder) {
        PointAddition partiallyUsedPoint = unusedPointsDescendingByCreatedAt.get(i - 1);
        List<PointAddition> pointsToBeUsed = unusedPointsDescendingByCreatedAt.subList(0, i - 1);
        List<PointUsage> collect = pointsToBeUsed.stream()
            .map(pointAddition -> PointUsage.from(pointAddition.getMemberId(), pointAddition.getOrderId(),
                pointAddition.getId(), pointAddition.getAmount()))
            .collect(toList());
        collect.add(PointUsage.from(partiallyUsedPoint.getMemberId(), partiallyUsedPoint.getOrderId(),
            partiallyUsedPoint.getId(), partiallyUsedPoint.getAmount() - remainder));
        return collect;
    }

    public int getPointsToBeExpired(int expirationDate) {
        List<PointAddition> unused = findUnusedPointsAscendingByExpireAt();
        LocalDateTime now = LocalDateTime.now();
        return unused.stream()
            .filter(pointAddition -> isGoingToBeExpired(expirationDate, now, pointAddition))
            .mapToInt(PointAddition::getAmount)
            .sum();
    }

    private boolean isGoingToBeExpired(int expirationDate, LocalDateTime now, PointAddition pointAddition) {
        return now.plusDays(expirationDate).isAfter(pointAddition.getExpireAt());
    }
}
