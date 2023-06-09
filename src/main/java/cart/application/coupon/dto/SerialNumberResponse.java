package cart.application.coupon.dto;

import cart.domain.coupon.SerialNumber;

public class SerialNumberResponse {

	private Long id;
	private String serialNumber;
	private boolean isIssued;

	public SerialNumberResponse() {
	}

	public SerialNumberResponse(final SerialNumber serialNumber) {
		this(serialNumber.getId(), serialNumber.getSerialNumber(), serialNumber.isIssued());
	}

	public SerialNumberResponse(final Long id, final String serialNumber, final boolean isIssued) {
		this.id = id;
		this.serialNumber = serialNumber;
		this.isIssued = isIssued;
	}

	public Long getId() {
		return id;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public boolean isIssued() {
		return isIssued;
	}
}
