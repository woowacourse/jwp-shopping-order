package cart.domain;

import cart.domain.point.Point;
import cart.domain.point.UsedPoint;

import java.util.ArrayList;
import java.util.List;

public class PurchaseOrder {
    private PurchaseOrderInfo purchaseOrderInfo;
    private List<PurchaseOrderItem> purchaseOrderItems;
    private List<UsedPoint> usedPoints;
    private Point rewardPoints;

    public PurchaseOrder(PurchaseOrderInfo purchaseOrderInfo, List<PurchaseOrderItem> purchaseOrderItems,
                         List<UsedPoint> usedPoints, Point rewardPoints) {
        this.purchaseOrderInfo = purchaseOrderInfo;
        this.purchaseOrderItems = new ArrayList<>(purchaseOrderItems);
        this.usedPoints = new ArrayList<>(usedPoints);
        this.rewardPoints = rewardPoints;
    }

    public void changeStatus(OrderStatus status) {
        this.purchaseOrderInfo.changeStatus(status);
    }

    public int getUsedPoint() {
        return usedPoints.stream()
                         .mapToInt(UsedPoint::getUsedPoint)
                         .sum();
    }

    public PurchaseOrderInfo getPurchaseOrderInfo() {
        return purchaseOrderInfo;
    }

    public List<PurchaseOrderItem> getPurchaseOrderItems() {
        return purchaseOrderItems;
    }

    public List<UsedPoint> getUsedPoints() {
        return usedPoints;
    }

    public Point getRewardPoints() {
        return rewardPoints;
    }
}
