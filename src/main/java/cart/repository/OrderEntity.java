package cart.repository;

public class OrderEntity {
    private final Long id;
    private final Long memberId;
    private final Long originalPrice;
    private final Long usedPoint;
    private final Long pointToAdd;
    
    public OrderEntity(
            final Long memberId,
            final Long originalPrice,
            final Long usedPoint,
            final Long pointToAdd
    ) {
        this(null, memberId, originalPrice, usedPoint, pointToAdd);
    }
    
    public OrderEntity(
            final Long id,
            final Long memberId,
            final Long originalPrice,
            final Long usedPoint,
            final Long pointToAdd
    ) {
        this.id = id;
        this.memberId = memberId;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }
    
    public Long getId() {
        return id;
    }
    
    public Long getMemberId() {
        return memberId;
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
