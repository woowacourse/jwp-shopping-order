package cart.application;

import cart.dao.*;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.point.*;
import cart.domain.purchaseorder.PurchaseOrder;
import cart.domain.purchaseorder.PurchaseOrderInfo;
import cart.domain.purchaseorder.PurchaseOrderItem;
import cart.dto.purchaseorder.request.PurchaseOrderItemRequest;
import cart.dto.purchaseorder.request.PurchaseOrderRequest;
import cart.exception.PurchaseOrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cart.domain.purchaseorder.OrderStatus.CANCELED;

@Transactional
@Service
public class PaymentService {

    private final ProductDao productDao;
    private final PurchaseOrderDao purchaseOrderDao;
    private final PurchaseOrderItemDao purchaseOrderItemDao;
    private final MemberRewardPointDao memberRewardPointDao;
    private final OrderMemberUsedPointDao orderMemberUsedPointDao;
    private final CartItemDao cartItemDao;

    public PaymentService(ProductDao productDao, PurchaseOrderDao purchaseOrderDao,
                          PurchaseOrderItemDao purchaseOrderItemDao, MemberRewardPointDao memberRewardPointDao,
                          OrderMemberUsedPointDao orderMemberUsedPointDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.purchaseOrderDao = purchaseOrderDao;
        this.purchaseOrderItemDao = purchaseOrderItemDao;
        this.memberRewardPointDao = memberRewardPointDao;
        this.orderMemberUsedPointDao = orderMemberUsedPointDao;
        this.cartItemDao = cartItemDao;
    }

    public Long createPurchaseOrder(Member member, PurchaseOrderRequest purchaseOrderRequest) {
        LocalDateTime now = LocalDateTime.now();

        List<PurchaseOrderItem> orderItems = getPurchaseOrderItems(purchaseOrderRequest);
        int payment = getPayment(purchaseOrderRequest, orderItems);
        Point rewardPoint = getPoint(now, payment);

        PurchaseOrderInfo purchaseOrderInfo = new PurchaseOrderInfo(member, now, payment, purchaseOrderRequest.getUsedPoint());
        List<UsedPoint> usedPoints = getUsedPoints(member, purchaseOrderRequest);
        return savePurchaseOrder(member, orderItems, rewardPoint, purchaseOrderInfo, usedPoints);
    }

    private int getPayment(PurchaseOrderRequest purchaseOrderRequest, List<PurchaseOrderItem> orderItems) {
        int payment = getAllItemPayment(orderItems);
        payment -= purchaseOrderRequest.getUsedPoint();
        return payment;
    }

    private int getAllItemPayment(List<PurchaseOrderItem> purchaseOrderItems) {
        return purchaseOrderItems.stream()
                                 .mapToInt(orderItem ->
                                         orderItem.getProduct()
                                                  .getPrice() * orderItem.getQuantity())
                                 .sum();
    }

    private static Point getPoint(LocalDateTime now, int payment) {
        int point = SavePointPolicy.calculate(payment);
        return new Point(point, now, SavePointPolicy.getExpiredAt(now));
    }

    private List<UsedPoint> getUsedPoints(Member member, PurchaseOrderRequest purchaseOrderRequest) {
        MemberPoints memberPoints = new MemberPoints(member, memberRewardPointDao.getAllByMemberId(member.getId()));
        List<Point> pointsSnapshot = getPointSnapshot(memberPoints.getPoints());

        List<UsedPoint> usedPoints = PointPolicy.usedPoint(memberPoints, purchaseOrderRequest.getUsedPoint());
        updateChangePoints(memberPoints.getPoints(), pointsSnapshot);
        return usedPoints;
    }

    private Long savePurchaseOrder(Member member, List<PurchaseOrderItem> orderItems, Point rewardPoint, PurchaseOrderInfo purchaseOrderInfo, List<UsedPoint> usedPoints) {
        PurchaseOrder purchaseOrder = new PurchaseOrder(purchaseOrderInfo, orderItems, usedPoints, rewardPoint);
        Long orderId = savePurchaseOrder(member, purchaseOrder);
        deleteCartItems(member, purchaseOrder.getPurchaseOrderItems());
        return orderId;
    }

    private List<PurchaseOrderItem> getPurchaseOrderItems(PurchaseOrderRequest purchaseOrderRequest) {
        List<Product> products = getAllProductsByIds(purchaseOrderRequest.getProducts());
        return getAllPurchaseOrderItem(purchaseOrderRequest, products);
    }

    private List<Product> getAllProductsByIds(List<PurchaseOrderItemRequest> purchaseOrderItems) {
        List<Long> itemIds = getItemIds(purchaseOrderItems);
        List<Product> products = productDao.getAllByIds(itemIds);

        if (products.size() == itemIds.size()) {
            return products;
        }
        throw new IllegalArgumentException("존재하지 않는 상품이 포함되어 있습니다.");
    }

    private List<Long> getItemIds(List<PurchaseOrderItemRequest> purchaseOrderItems) {
        return purchaseOrderItems.stream()
                                 .map(PurchaseOrderItemRequest::getProductId)
                                 .collect(Collectors.toList());
    }

    private List<PurchaseOrderItem> getAllPurchaseOrderItem(PurchaseOrderRequest purchaseOrderRequest,
                                                            List<Product> products) {
        List<PurchaseOrderItem> purchaseOrderItems = new ArrayList<>();
        for (PurchaseOrderItemRequest purchaseOrderItemRequest : purchaseOrderRequest.getProducts()) {
            Product product = getProductById(products, purchaseOrderItemRequest.getProductId());
            PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem(product, purchaseOrderItemRequest.getQuantity());
            purchaseOrderItems.add(purchaseOrderItem);
        }
        return purchaseOrderItems;
    }

    private Product getProductById(List<Product> products, Long id) {
        return products.stream()
                       .filter(product -> product.isMatchId(id))
                       .findFirst()
                       .orElseThrow(() -> new PurchaseOrderException("존재하지 않는 상품이 포함되어 있습니다."));
    }

    private List<Point> getPointSnapshot(List<Point> points) {
        List<Point> pointSnapshot = new ArrayList<>();
        for (Point point : points) {
            pointSnapshot.add(new Point(point.getId(), point.getPointAmount(),
                    point.getCreatedAt(), point.getExpiredAt()));
        }
        return pointSnapshot;
    }

    private Long savePurchaseOrder(Member member, PurchaseOrder purchaseOrder) {
        PurchaseOrderInfo purchaseOrderInfo = purchaseOrder.getPurchaseOrderInfo();

        Long saveOrderId = purchaseOrderDao.save(purchaseOrderInfo);
        purchaseOrderItemDao.saveAll(purchaseOrder.getPurchaseOrderItems(), saveOrderId);
        orderMemberUsedPointDao.saveAll(purchaseOrder.getUsedPoints(), saveOrderId);
        memberRewardPointDao.save(member.getId(), purchaseOrder.getRewardPoints(), saveOrderId);
        return saveOrderId;
    }

    private void deleteCartItems(Member member, List<PurchaseOrderItem> purchaseOrderItems) {
        List<Long> productIds = purchaseOrderItems.stream()
                                                  .map(PurchaseOrderItem::getProductId)
                                                  .collect(Collectors.toList());
        cartItemDao.deleteByProductIds(member.getId(), productIds);
    }

    private void updateChangePoints(List<Point> memberPoints, List<Point> pointsSnapshot) {
        List<Point> updatePoints = getDifference(memberPoints, pointsSnapshot);
        memberRewardPointDao.updatePoints(updatePoints);
    }

    private List<Point> getDifference(List<Point> points, List<Point> removePoints) {
        Set<Point> updatePoints = new HashSet<>(points);
        Set<Point> remove = new HashSet<>(removePoints);
        updatePoints.removeAll(remove);
        return List.copyOf(updatePoints);
    }

    public void deleteOrder(Member member, Long orderId) {
        checkCanceledOrder(orderId);
        Point rewardPointByOrder = memberRewardPointDao.getPointByOrderId(orderId)
                                                       .orElseThrow(() -> new IllegalArgumentException("삭제 중 문제가 발생했습니다."));
        updatePurchaseOrderStatus(member, orderId);
        checkAlreadyUsed(rewardPointByOrder);
        updateUsedAndRewardPoint(member, orderId, rewardPointByOrder);
    }

    private void checkCanceledOrder(Long orderId) {
        if (purchaseOrderDao.isCanceled(orderId)) {
            throw new IllegalArgumentException("이미 취소된 주문입니다.");
        }
    }

    private void updatePurchaseOrderStatus(Member member, Long orderId) {
        purchaseOrderDao.findById(orderId)
                        .ifPresent(purchaseOrderInfo -> {
                                    purchaseOrderInfo.checkOwner(member);
                                    purchaseOrderInfo.changeStatus(CANCELED);
                                    purchaseOrderInfo.updatePayment(0);
                                    purchaseOrderInfo.updateUsedPoint(0);
                                    purchaseOrderDao.updateStatus(purchaseOrderInfo);
                                }
                        );
    }

    private void checkAlreadyUsed(Point rewardPoint) {
        if (orderMemberUsedPointDao.isAlreadyUsedReward(rewardPoint.getId())) {
            throw new IllegalArgumentException("해당 주문을 통해 적립된 포인트를 이미 사용해 취소할 수 없습니다.");
        }
    }

    private void updateUsedAndRewardPoint(Member member, Long orderId, Point rewardPointByOrder) {
        List<Point> rewardPointByMember = memberRewardPointDao.getAllByMemberId(member.getId());
        memberRewardPointDao.deleteById(rewardPointByOrder.getId());

        MemberPoints memberPoints = new MemberPoints(member, rewardPointByMember);
        List<UsedPoint> usedPoints = orderMemberUsedPointDao.getAllUsedPointByOrderId(orderId);
        memberPoints.cancelledPoints(usedPoints);
        memberRewardPointDao.updatePoints(memberPoints.getPoints());
        orderMemberUsedPointDao.deleteAll(usedPoints);
    }
}
