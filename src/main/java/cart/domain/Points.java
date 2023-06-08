package cart.domain;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    // 가진 포인트 중 금액에 따라 사용 기록에 추가될 PointUsage 객체들을 결정한다
    public List<PointUsage> decidePointsToBeUsed(int usePointAmount) {
        List<PointAddition> unusedPointsAscendingByExpireAt = findUnusedPointsAscendingByExpireAt();

        return getPointsToBeUsed(usePointAmount, unusedPointsAscendingByExpireAt);
    }

    // 획득 포인트 중 미사용 포인트를 오래된 것부터 정렬하여 반환한다
    private List<PointAddition> findUnusedPointsAscendingByExpireAt() {
        Map<Long, List<PointUsage>> usagesByPointAdditionId = pointUsages.stream()
            .collect(groupingBy(PointUsage::getPointAdditionId));
        List<PointAddition> unusedPoints = new ArrayList<>();
        List<PointAddition> partiallyUsedPoints = new ArrayList<>();
        for (PointAddition addition : pointAdditions) {
            // 특정한 획득 포인트의 사용 기록이 존재하지 않는다면 미사용 포인트 리스트에 추가한다
            if (!usagesByPointAdditionId.containsKey(addition.getId())) {
                unusedPoints.add(addition);
                continue;
            }
            // 특정한 획득 포인트가 분할 사용되었음에도 다 사용되지 않았다면 부분 사용 포인트 리스트에 추가한다
            List<PointUsage> usages = usagesByPointAdditionId.get(addition.getId());
            int amount = usages.stream()
                .mapToInt(PointUsage::getAmount)
                .sum();
            if (addition.getAmount() != amount) {
                // 이 때 분할 사용된 포인트는 남은 액수로 변환하여 저장한다
                partiallyUsedPoints.add(addition.reduce(amount));
            }
        }
        // 관리가 의도대로 되었다면 부분 사용된 포인트는 없거나 가장 오래된 하나만 존재한다
        assertContainingOnePartiallyUsedPointAtMax(partiallyUsedPoints);
        // 부분 사용 포인트 또한 미사용 포인트이므로 이를 추가, 정렬하여 반환한다
        unusedPoints.addAll(partiallyUsedPoints);
        return unusedPoints.stream()
            .sorted(comparing(PointAddition::getExpireAt))
            .collect(toList());
    }

    private void assertContainingOnePartiallyUsedPointAtMax(List<PointAddition> partiallyUsedPoints) {
        if (partiallyUsedPoints.size() > 1) {
            throw new IllegalStateException("부분적으로 사용된 포인트가 한 개 이상입니다");
        }
    }

    // 오래된 것부터 정렬된 미사용 포인트 중, 사용할 포인트의 양에 해당하는 만큼의 사용 처리 포인트 객체들을 만들어 반환한다
    private List<PointUsage> getPointsToBeUsed(int usePointAmount, List<PointAddition> unusedPointsDescendingByCreatedAt) {
        int pointSum = 0;
        int newestPointToBeUsedIndex = 0;
        // 오래된 포인트들부터 합산하여 사용할 포인트 양을 넘는 인덱스를 찾는다
        while (pointSum < usePointAmount) {
            pointSum += unusedPointsDescendingByCreatedAt.get(newestPointToBeUsedIndex).getAmount();
            newestPointToBeUsedIndex++;
        }
        // 사용될 포인트 중 가장 최신 포인트가 남김 없이 사용되었는지 여부에 따라 다르게 처리한다
        int remainder = pointSum - usePointAmount;
        if (remainder == 0) {
            return getPointsToBeUsedWithoutRemainder(unusedPointsDescendingByCreatedAt, newestPointToBeUsedIndex);
        }
        return getPointsToBeUsedWithRemainder(unusedPointsDescendingByCreatedAt, newestPointToBeUsedIndex, remainder);
    }

    // 사용될 가장 최신 포인트가 나머지 없이 사용되는 경우, 미사용 포인트부터 사용될 가장 최신 포인트까지를 사용 처리 포인트 객체로 변환한다
    private List<PointUsage> getPointsToBeUsedWithoutRemainder(List<PointAddition> unusedPointsDescendingByCreatedAt,
        int newestPointToBeUsedIndex) {
        List<PointAddition> pointsToBeUsed = unusedPointsDescendingByCreatedAt.subList(0, newestPointToBeUsedIndex);
        return pointsToBeUsed.stream()
            .map(pointAddition -> PointUsage.from(pointAddition.getMemberId(), pointAddition.getOrderId(),
                pointAddition.getId(), pointAddition.getAmount()))
            .collect(toList());
    }

    // 사용될 가장 최신 포인트가 분할 사용되는 경우, 미사용 포인트부터 사용될 가장 최신 포인트 이전까지를 사용 처리 포인트 객체로 변환하고, 최신 포인트는 부분적으로만 사용 처리한다
    private List<PointUsage> getPointsToBeUsedWithRemainder(List<PointAddition> unusedPointsDescendingByCreatedAt,
        int newestPointToBeUsedIndex, int remainder) {
        PointAddition partiallyUsedPoint = unusedPointsDescendingByCreatedAt.get(newestPointToBeUsedIndex - 1);
        List<PointAddition> pointsToBeUsed = unusedPointsDescendingByCreatedAt.subList(0, newestPointToBeUsedIndex - 1);
        List<PointUsage> collect = pointsToBeUsed.stream()
            .map(pointAddition -> PointUsage.from(pointAddition.getMemberId(), pointAddition.getOrderId(),
                pointAddition.getId(), pointAddition.getAmount()))
            .collect(toList());
        collect.add(PointUsage.from(partiallyUsedPoint.getMemberId(), partiallyUsedPoint.getOrderId(),
            partiallyUsedPoint.getId(), partiallyUsedPoint.getAmount() - remainder));
        return collect;
    }

    public int sumGivenExpirationDateRemaining(int expirationDate) {
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
