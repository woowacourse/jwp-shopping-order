package cart.dao.entity;

import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.vo.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final BigDecimal totalPrice;
    private final BigDecimal usePoint;
    private final BigDecimal deliveryFee;
    private final LocalDateTime createdAt;

    public OrderEntity(Long memberId, BigDecimal totalPrice, BigDecimal usePoint, BigDecimal deliveryFee, LocalDateTime createdAt) {
        this(null, memberId, totalPrice, usePoint, deliveryFee, createdAt);
    }

    public OrderEntity(Long id, Long memberId, BigDecimal totalPrice, BigDecimal usePoint, BigDecimal deliveryFee, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.totalPrice = totalPrice;
        this.usePoint = usePoint;
        this.deliveryFee = deliveryFee;
        this.createdAt = createdAt;
    }

    public static OrderEntity toEntity(Order order) {
        return new OrderEntity(order.getId(),
                order.getMember().getId(),
                order.getTotalPrice().getValue(),
                order.getUsePoint().getValue(),
                order.getDeliveryFee().getValue(),
                order.getCreatedAt()
        );
    }

    public Order toDomain(Member member, CartItems cartItems) {
        return new Order(id, member, cartItems, Money.from(usePoint), Money.from(deliveryFee), createdAt);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BigDecimal getUsePoint() {
        return usePoint;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", totalPrice=" + totalPrice +
                ", usePoint=" + usePoint +
                ", deliveryFee=" + deliveryFee +
                ", createdAt=" + createdAt +
                '}';
    }
}
