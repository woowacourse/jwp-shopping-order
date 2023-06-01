package cart.domain;

public class Order {

    private Long id;
    private final Member member;
    private final Long originalPrice;
    private final Long usedPoint;
    private final Long pointToAdd;

    public Order(Member member, Long originalPrice, Long usedPoint, Long pointToAdd) {
        this.member = member;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    public Order(Long id, Member member, Long originalPrice, Long usedPoint, Long pointToAdd) {
        this.id = id;
        this.member = member;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    public void applyPoint() {
        member.minusPoint(usedPoint);
        member.plusPoint(pointToAdd);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public Long getUsedPoint() {
        return usedPoint;
    }

    public Long getPointToAdd() {
        return pointToAdd;
    }
}
