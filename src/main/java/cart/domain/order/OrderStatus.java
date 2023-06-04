package cart.domain.order;

public enum OrderStatus {
	PAID("결제 완료"),
	CANCELED("주문 취소"),
	IN_TRANSIT("배송 중");

	private final String status;

	OrderStatus(final String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
