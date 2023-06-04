package cart.domain.order;

import cart.domain.member.Member;
import cart.exception.authorization.OrderException;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {

    private final Long id;
    private final Member member;
    private final UsedPoint usedPoint;
    private final SavedPoint savedPoint;
    private final DeliveryFee deliveryFee;
    private final LocalDateTime orderedAt;

    public Order(final Member member, final UsedPoint usedPoint, final SavedPoint savedPoint, final DeliveryFee deliveryFee) {
        this(-1, member, usedPoint, savedPoint, deliveryFee, null);
    }

    public Order(final long id, final Member member,
                 final UsedPoint usedPoint,
                 final SavedPoint savedPoint,
                 final DeliveryFee deliveryFee,
                 final LocalDateTime orderedAt) {
        this.id = id;
        this.member = member;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.deliveryFee = deliveryFee;
        this.orderedAt = orderedAt;
    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member, member)) {
            throw new OrderException(member.getEmail());
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public UsedPoint getUsedPoint() {
        return usedPoint;
    }

    public Integer getUsedPointValue() {
        return usedPoint.getUsedPoint();
    }

    public SavedPoint getSavedPoint() {
        return savedPoint;
    }

    public Integer getSavedPointValue() {
        return savedPoint.getSavedPoint();
    }

    public DeliveryFee getDeliveryFee() {
        return deliveryFee;
    }

    public int getDeliveryFeeValue() {
        return deliveryFee.getDeliveryFee();
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", member=" + member +
                ", usedPoint=" + usedPoint +
                ", savedPoint=" + savedPoint +
                ", deliveryFee=" + deliveryFee +
                ", orderedAt=" + orderedAt +
                '}';
    }
}
