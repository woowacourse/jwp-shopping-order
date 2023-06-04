package cart.application;

import cart.dao.*;
import cart.domain.*;
import cart.domain.point.MemberPoints;
import cart.domain.point.Point;
import cart.domain.point.SavePointPolicy;
import cart.domain.point.UsedPoint;
import cart.dto.purchaseorder.PurchaseOrderItemRequest;
import cart.dto.purchaseorder.PurchaseOrderRequest;
import cart.exception.PurchaseOrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    // TODO: 2023/06/02 한 메서드가 너무 긴 상태..! 수정 필요
    public Long createPurchaseOrder(Member member, PurchaseOrderRequest purchaseOrderRequest) {
        List<PurchaseOrderItem> orderItems = getPurchaseOrderItems(purchaseOrderRequest);
        int payment = getPayment(orderItems);
        payment -= purchaseOrderRequest.getUsedPoint();
        int point = SavePointPolicy.calculate(payment);

        LocalDateTime now = LocalDateTime.now();
        Point rewardPoint = new Point(point, now, SavePointPolicy.getExpiredAt(now));
        PurchaseOrderInfo purchaseOrderInfo = new PurchaseOrderInfo(member, now, payment, purchaseOrderRequest.getUsedPoint());

        MemberPoints memberPoints = new MemberPoints(member, memberRewardPointDao.getAllByMemberId(member.getId()));
        List<Point> pointsSnapshot = getPointSnapshot(memberPoints.getPoints());

        List<UsedPoint> usedPoints = memberPoints.usedPoint(purchaseOrderRequest.getUsedPoint());

        PurchaseOrder purchaseOrder = new PurchaseOrder(purchaseOrderInfo, orderItems, usedPoints, rewardPoint);
        Long orderId = savePurchaseOrder(member, purchaseOrder);
        deleteCartItems(member, purchaseOrder.getPurchaseOrderItems());
        updateChangePoints(memberPoints.getPoints(), pointsSnapshot);
        return orderId;
    }

    private List<PurchaseOrderItem> getPurchaseOrderItems(PurchaseOrderRequest purchaseOrderRequest) {
        List<Product> products = getAllProductsByIds(purchaseOrderRequest.getPurchaseOrderItems());
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
        for (PurchaseOrderItemRequest purchaseOrderItemRequest : purchaseOrderRequest.getPurchaseOrderItems()) {
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

    private int getPayment(List<PurchaseOrderItem> purchaseOrderItems) {
        return purchaseOrderItems.stream()
                                 .mapToInt(orderItem ->
                                         orderItem.getProduct()
                                                  .getPrice() * orderItem.getQuantity())
                                 .sum();
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
}
