package cart.application;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.dto.PostOrderRequest;
import cart.application.dto.SingleKindProductRequest;
import cart.dao.OrderDao;
import cart.dao.PointAdditionDao;
import cart.dao.PointUsageDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderStatus;
import cart.domain.PointAddition;
import cart.domain.PointCalculator;
import cart.domain.PointUsage;
import cart.domain.Product;
import cart.domain.QuantityAndProduct;
import cart.exception.IllegalOrderException;

@Service
public class AddOrderService {

    private static final int POINT_EXPIRATION_DATE = 90;

    private final ApplicationEventPublisher publisher;
    private final PointCalculator pointCalculator;
    private final PointAdditionDao pointAdditionDao;
    private final PointUsageDao pointUsageDao;
    private final OrderDao orderDao;
    private final ProductDao productDao;

    public AddOrderService(ApplicationEventPublisher publisher, PointCalculator pointCalculator, PointAdditionDao pointAdditionDao,
        PointUsageDao pointUsageDao, OrderDao orderDao,
        ProductDao productDao) {
        this.publisher = publisher;
        this.pointCalculator = pointCalculator;
        this.pointAdditionDao = pointAdditionDao;
        this.pointUsageDao = pointUsageDao;
        this.orderDao = orderDao;
        this.productDao = productDao;
    }

    @Transactional
    public Long addOrder(Member member, PostOrderRequest request) {
        // 1. 요청에서 포인트를 사용하는지 확인한다.
        // 1.(a) 포인트를 사용한다면 해당 멤버의 현 포인트를 가져온다
        // 1.(b) 사용 예정 포인트가 현 보유 포인트보다 작거나 같은지 검증한다
        List<PointAddition> pointAdditions = new ArrayList<>();
        List<PointUsage> pointUsages = new ArrayList<>();
        int usedPoint = request.getUsedPoint();
        if (usedPoint != 0) {
            pointAdditions = pointAdditionDao.findAllByMemberId(member.getId());
            pointUsages = pointUsageDao.findAllByMemberId(member.getId());
            validateHavingEnoughPoint(pointAdditions, pointUsages, usedPoint);
        }

        // 2. 요청된 상품의 id가 유효한지 검증한다.
        List<SingleKindProductRequest> requestProducts = request.getProducts();
        Set<Long> productIds = requestProducts.stream()
            .map(SingleKindProductRequest::getProductId)
            .collect(toSet());
        List<Product> foundProducts = productDao.getProductsByIds(new ArrayList<>(productIds));
        validateProductsPresence(productIds, foundProducts);
        // 3. 지불 총액을 계산한다.
        Map<Long, Product> productsById = foundProducts.stream()
            .collect(toMap(Product::getId, Function.identity()));
        List<QuantityAndProduct> quantityAndProducts = requestProducts.stream()
            .map(product -> new QuantityAndProduct(product.getQuantity(), productsById.get(product.getProductId())))
            .collect(toList());
        int totalPrice = quantityAndProducts.stream()
            .mapToInt(q -> q.getQuantity() * q.getProduct().getPrice())
            .sum();
        // 4. 지불 총액보다 사용 포인트가 크지 않은지 검증한다.
        validateNotUsingMorePointThanPrice(usedPoint, totalPrice);
        // 5. 주문 내역을 등록한다.
        LocalDateTime now = LocalDateTime.now();
        int payAmount = totalPrice - usedPoint;
        int savedPoint = pointCalculator.calculatePoint(payAmount);
        Long orderId = orderDao.insert(new Order(member, now, payAmount, usedPoint, savedPoint, OrderStatus.PENDING,
            quantityAndProducts));
        // 6. 멤버의 포인트를 감액한다.
        if (usedPoint != 0) {
            reducePoint(member, pointAdditions, pointUsages, usedPoint, orderId);
        }
        // 7. 멤버의 포인트를 증액한다.
        pointAdditionDao.insert(PointAddition.from(member.getId(), orderId, savedPoint, now, POINT_EXPIRATION_DATE));
        return orderId;
    }

    private void validateHavingEnoughPoint(List<PointAddition> pointAdditions, List<PointUsage> pointUsages,
        int usedPoint) {
        int remainingPoint = getRemainingPoint(pointAdditions, pointUsages);
        if (remainingPoint < usedPoint) {
            throw new IllegalOrderException("보유한 포인트 이상을 사용할 수 없습니다");
        }
    }

    private int getRemainingPoint(List<PointAddition> pointAdditions, List<PointUsage> pointUsages) {
        int totalAddition = pointAdditions.stream()
            .mapToInt(PointAddition::getAmount)
            .sum();
        int totalUsage = pointUsages.stream()
            .mapToInt(PointUsage::getAmount)
            .sum();
        return totalAddition - totalUsage;
    }

    private void validateProductsPresence(Set<Long> productIds, List<Product> foundProducts) {
        if (productIds.size() != foundProducts.size()) {
            throw new IllegalOrderException("존재하지 않는 상품을 주문할 수 없습니다");
        }
    }

    private void validateNotUsingMorePointThanPrice(int usedPoint, int totalPrice) {
        if (usedPoint > totalPrice) {
            throw new IllegalOrderException("지불할 금액을 초과하는 포인트를 사용할 수 없습니다");
        }
    }

    private void reducePoint(Member member, List<PointAddition> pointAdditions, List<PointUsage> pointUsages,
        int usedPoint, Long orderId) {
        Map<Long, PointUsage> pointUsageByPointAdditionId = pointUsages.stream()
            .collect(toMap(PointUsage::getPointAdditionId, Function.identity()));
        // 미사용 포인트들을 찾는다
        List<PointAddition> unusedPointsDescendingByCreatedAt = pointAdditions.stream()
            .filter(pointAddition -> pointUsageByPointAdditionId.containsKey(pointAddition.getId()))
            .sorted(Comparator.comparing(PointAddition::getCreatedAt).reversed())
            .collect(toList());

        // 미사용 포인트 중 사용 처리될 포인트를 모은다(가장 최신의 일부만 사용될 수 있는 포인트 포함)
        List<PointUsage> converted = new ArrayList<>();
        accumulateUsingPoints(member, usedPoint, orderId, unusedPointsDescendingByCreatedAt, converted);
        pointUsageDao.insertAll(converted);
    }

    private void accumulateUsingPoints(Member member, int usedPoint, Long orderId,
        List<PointAddition> unusedPointsDescendingByCreatedAt, List<PointUsage> converted) {
        List<PointAddition> usingPoints = new ArrayList<>();
        int pointSum = 0;
        for (PointAddition pointAddition : unusedPointsDescendingByCreatedAt) {
            pointSum += pointAddition.getAmount();
            usingPoints.add(pointAddition);
            if (pointSum >= usedPoint) {
                break;
            }
        }
        // 마지막 30원 중 20원만 써서 총 130포인트에 120포인트 씀. 10포인트만 썼다고 기록해야..
        handleIfPointPartiallyRemains(member, usedPoint, orderId, usingPoints, pointSum, converted);
        usingPoints.forEach(
            point -> converted.add(PointUsage.from(member.getId(), orderId, point.getId(), point.getAmount())));
    }

    private void handleIfPointPartiallyRemains(Member member, int usedPoint, Long orderId,
        List<PointAddition> usingPoints,
        int pointSum, List<PointUsage> converted) {
        int remainder = pointSum - usedPoint;
        if (remainder > 0) {
            // 사용 처리될 포인트 중 일부만 사용될 가장 최신의 포인트를 가져온다.
            PointAddition partiallyUsingPoint = usingPoints.get(usingPoints.size() - 1);
            usingPoints.remove(usingPoints.size() - 1);
            converted.add(PointUsage.from(member.getId(), orderId, partiallyUsingPoint.getId(), remainder));
        }
    }
}
