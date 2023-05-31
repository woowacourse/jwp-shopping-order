package cart.domain.order;

import cart.error.exception.OrderException;

public class DeliveryFee {

	public static final Long DEFAULT = 3_000L;

	private final Long deliveryFee;

	public DeliveryFee(final Long deliveryFee) {
		validatePositive(deliveryFee);
		this.deliveryFee = deliveryFee;
	}

	public DeliveryFee() {
		this.deliveryFee = DEFAULT;
	}

	public Long getDeliveryFee() {
		return deliveryFee;
	}

	private static void validatePositive(final Long deliveryFee) {
		if (deliveryFee < 0) {
			throw new OrderException.BadRequest("배송비는 0원 미만이 될 수 없습니다.");
		}
	}
}
