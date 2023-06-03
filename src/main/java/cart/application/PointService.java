package cart.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import cart.application.event.PointAdditionEvent;
import cart.application.event.PointRetrieveEvent;
import cart.dao.PointAdditionDao;
import cart.dao.PointUsageDao;
import cart.domain.PointAddition;
import cart.domain.PointCalculator;
import cart.domain.PointUsage;
import cart.domain.Points;
import cart.exception.IllegalOrderException;
import cart.exception.IllegalPointException;

@Service
public class PointService {

    private static final int POINT_EXPIRATION_DATE = 90;

    private final PointCalculator pointCalculator;
    private final PointAdditionDao pointAdditionDao;
    private final PointUsageDao pointUsageDao;

    public PointService(PointCalculator pointCalculator, PointAdditionDao pointAdditionDao,
        PointUsageDao pointUsageDao) {
        this.pointCalculator = pointCalculator;
        this.pointAdditionDao = pointAdditionDao;
        this.pointUsageDao = pointUsageDao;
    }

    public int getIncreasedPointAmountByMemberId(long memberId) {
        List<PointAddition> additions = pointAdditionDao.findAllByMemberId(memberId);
        return additions.stream()
            .mapToInt(PointAddition::getAmount)
            .sum();
    }

    public int getUsedPointAmountByMemberId(long memberId) {
        List<PointUsage> usages = pointUsageDao.findAllByMemberId(memberId);
        return usages.stream()
            .mapToInt(PointUsage::getAmount)
            .sum();
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handlePointProcessInOrder(PointAdditionEvent event) {
        long memberId = event.getMemberId();
        int usePointAmount = event.getUsePointAmount();
        int payAmount = event.getPayAmount();
        if (usePointAmount != 0) {
            validateNotUsingMorePointThanPrice(usePointAmount, payAmount);
            usePoint(memberId, usePointAmount);
        }
        givePoint(event.getOrderId(), memberId, payAmount, event.getNow());
    }

    private void validateNotUsingMorePointThanPrice(int usedPoint, int payAmount) {
        if (usedPoint > payAmount) {
            throw new IllegalPointException("지불할 금액을 초과하는 포인트를 사용할 수 없습니다");
        }
    }

    private void usePoint(long memberId, int usePointAmount) {
        List<PointAddition> pointAdditions = pointAdditionDao.findAllByMemberId(memberId);
        List<PointUsage> pointUsages = pointUsageDao.findAllByMemberId(memberId);
        Points points = new Points(pointAdditions, pointUsages);
        validateIsPointEnough(usePointAmount, points);
        List<PointUsage> pointsToBeUsed = points.findPointsToBeUsed(usePointAmount);
        pointUsageDao.insertAll(pointsToBeUsed);
    }

    private void validateIsPointEnough(int usePointAmount, Points points) {
        if (points.hasNotEnoughPoint(usePointAmount)) {
            throw new IllegalPointException("보유한 포인트 이상을 사용할 수 없습니다");
        }
    }

    private void givePoint(long orderId, long memberId, int payAmount, LocalDateTime now) {
        int addition = pointCalculator.calculatePoint(payAmount);
        pointAdditionDao.insert(
            PointAddition.from(memberId, orderId, addition, now, now.plusDays(POINT_EXPIRATION_DATE)));
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handlePointRetrieval(PointRetrieveEvent event) {
        long orderId = event.getOrderId();
        List<PointUsage> pointUsageHistory = pointUsageDao.findAllByOrderId(orderId);
        validateAddedOrderNotUsedYet(pointUsageHistory);
        pointAdditionDao.deleteByOrderId(orderId);
    }

    private void validateAddedOrderNotUsedYet(List<PointUsage> pointUsageHistory) {
        if (pointUsageHistory.size() != 0) {
            throw new IllegalOrderException("지급된 포인트가 이미 사용되어 취소가 불가능합니다");
        }
    }
}



