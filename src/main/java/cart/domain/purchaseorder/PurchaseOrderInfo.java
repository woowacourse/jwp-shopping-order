package cart.domain.purchaseorder;

import cart.domain.Member;
import cart.exception.PurchaseOrderException;

import java.time.LocalDateTime;
import java.util.Objects;

public class PurchaseOrderInfo {
    private Long id;
    private final Member member;
    private final LocalDateTime orderAt;
    private Integer payment;
    private Integer usedPoint;
    private OrderStatus status;

    public PurchaseOrderInfo(Member member, LocalDateTime orderAt, Integer payment, Integer usedPoint) {
        this.member = member;
        this.orderAt = orderAt;
        this.payment = payment;
        this.usedPoint = usedPoint;
        this.status = OrderStatus.PENDING;
    }

    public PurchaseOrderInfo(Member member, LocalDateTime orderAt,
                             Integer payment, Integer usedPoint, OrderStatus status) {
        this.member = member;
        this.orderAt = orderAt;
        this.payment = payment;
        this.usedPoint = usedPoint;
        this.status = status;
    }

    public PurchaseOrderInfo(Long id, Member member, LocalDateTime orderAt,
                             Integer payment, Integer usedPoint, OrderStatus status) {
        this.id = id;
        this.member = member;
        this.orderAt = orderAt;
        this.payment = payment;
        this.usedPoint = usedPoint;
        this.status = status;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new PurchaseOrderException.IllegalMember(this, member);
        }
    }

    public void changeStatus(OrderStatus status) {
        this.status = status;
    }

    public void updatePayment(int payment) {
        this.payment = payment;
    }
    public void updateUsedPoint(int usedPoint) {
        this.usedPoint = usedPoint;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LocalDateTime getOrderAt() {
        return orderAt;
    }

    public Integer getPayment() {
        if (status.equals(OrderStatus.CANCELED)) {
            return 0;
        }
        return payment;
    }

    public Integer getUsedPoint() {
        if (status.equals(OrderStatus.CANCELED)) {
            return 0;
        }
        return usedPoint;
    }

    public String getStatus() {
        return status.getValue();
    }

    @Override
    public String toString() {
        return "PurchaseOrderInfo{" +
                "id=" + id +
                ", member=" + member +
                ", orderAt=" + orderAt +
                ", payment=" + payment +
                ", usedPoint=" + usedPoint +
                ", status=" + status +
                '}';
    }
}
