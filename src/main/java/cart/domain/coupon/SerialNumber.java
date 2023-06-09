package cart.domain.coupon;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cart.error.exception.BadRequestException;

public class SerialNumber {

	private final Long id;
	private final String serialNumber;
	private final boolean isIssued;

	public SerialNumber(final Long id, final String serialNumber, final boolean isIssued) {
		this.id = id;
		this.serialNumber = serialNumber;
		this.isIssued = isIssued;
	}

	public static List<SerialNumber> generateSerialNumbers(final Integer couponCount) {
		validatePositiveCount(couponCount);
		List<SerialNumber> serialNumbers = new ArrayList<>();
		for (int i = 0; i < couponCount; i++) {
			serialNumbers.add(new SerialNumber(null, UUID.randomUUID().toString(), false));
		}
		return serialNumbers;
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

	private static void validatePositiveCount(final int count) {
		if (count <= 0) {
			throw new BadRequestException.SerialNumber();
		}
	}
}
