package cart.domain.order;

public interface OrderRepository {

    Long save(final Order order);

    Long saveWithCoupon(final Order order);

    Long countByMemberId(final Long memberId);
}
