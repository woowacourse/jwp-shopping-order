package cart.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import cart.application.dto.GetPointResponse;
import cart.application.event.PointAdditionEvent;
import cart.application.event.PointRetrieveEvent;
import cart.dao.PointAdditionDao;
import cart.dao.PointUsageDao;
import cart.domain.Member;
import cart.domain.PointAddition;
import cart.domain.PointCalculator;
import cart.domain.PointUsage;
import cart.domain.Points;
import cart.exception.IllegalOrderException;
import cart.exception.IllegalPointException;

@Service
public class PointService {

    private static final int POINT_EXPIRATION_DATE = 90;
    private static final int TO_BE_EXPIRED = 30;

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
            throw new IllegalPointException.UsingMorePointThanPrice();
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
            throw new IllegalPointException.PointIsNotEnough();
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

    @Transactional(readOnly = true)
    public GetPointResponse getPointStatus(Member member) {
        List<PointAddition> additions = pointAdditionDao.findAllByMemberId(member.getId());
        List<PointUsage> usages = pointUsageDao.findAllByMemberId(member.getId());
        Points points = new Points(additions, usages);
        int remaining = points.getTotalRemainingPoint();
        int toBeExpired = points.getPointsToBeExpired(TO_BE_EXPIRED);
        return new GetPointResponse(remaining, toBeExpired);
    }
}



