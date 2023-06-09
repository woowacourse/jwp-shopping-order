package cart.domain.order;

public enum OrderStatus {
	PAID("결제완료"),
	CANCELED("결제취소"),
	IN_TRANSIT("배송중");

	private final String status;

	OrderStatus(final String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
