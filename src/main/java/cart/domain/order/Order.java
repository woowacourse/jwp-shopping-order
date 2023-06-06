package cart.domain.order;

import cart.domain.member.Member;
import cart.exception.badrequest.order.OrderOwnerException;
import cart.exception.badrequest.order.OrderPointException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {

    private Long id;
    private Member member;
    private OrderProducts orderProducts;
    private Point usedPoint;
    private Point savedPoint;
    private Fee deliveryFee;
    private LocalDateTime orderedAt;

    private Order() {
    }

    public Order(Member member, List<OrderProduct> orderProducts, int usedPoint) {
        this(null, member, orderProducts, usedPoint, null);
    }

    public Order(
            Long id,
            Member member,
            List<OrderProduct> orderProducts,
            int usedPoint,
            LocalDateTime orderedAt
    ) {
        this.id = id;
        this.member = member;
        this.orderProducts = new OrderProducts(orderProducts);
        this.usedPoint = new Point(usedPoint);
        this.savedPoint = Point.from(this.orderProducts.calculateTotalPrice());
        this.deliveryFee = Fee.from(this.orderProducts.calculateTotalPrice());
        this.orderedAt = orderedAt;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new OrderOwnerException();
        }
    }

    public int calculateTotalPrice() {
        return orderProducts.calculateTotalPrice();
    }

    public int getTotalPrice() {
        return orderProducts.calculateTotalPrice() + deliveryFee.getValue();
    }

    public void checkPointAvailable(int point) {
        if (point > getTotalPrice()) {
            throw new OrderPointException("사용하려는 포인트가 총 결제 금액보다 많습니다. 총 결제 금액: " + getTotalPrice() + ", 사용 포인트: " + point);
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts.getValue();
    }

    public int getUsedPoint() {
        return usedPoint.getValue();
    }

    public int getSavedPoint() {
        return savedPoint.getValue();
    }

    public int getDeliveryFee() {
        return deliveryFee.getValue();
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
