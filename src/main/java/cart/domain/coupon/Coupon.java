package cart.domain.coupon;

import cart.exception.BadRequestException;

import java.time.LocalDateTime;

import static cart.exception.ErrorCode.*;

public class Coupon {

    private final Long id;
    private final String name;
    private final int discountRate;
    private final int period;
    private final LocalDateTime expiredAt;

    public Coupon(final String name, final int discountRate, final int period, final LocalDateTime expiredAt) {
        this(null, name, discountRate, period, expiredAt);
    }

    public Coupon(final Long id, final String name, final int discountRate, final int period, final LocalDateTime expiredAt) {
        validate(name, discountRate, period, expiredAt);
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
        this.period = period;
        this.expiredAt = expiredAt;
    }

    private void validate(final String name, final int discountRate, final int period, final LocalDateTime expiredAt) {
        validateName(name);
        validateDiscountRate(discountRate);
        validatePeriod(period);
        validateExpiredAt(expiredAt);
    }

    private void validateName(final String name) {
        if (name.length() < 1 || name.length() > 50) {
            throw new BadRequestException(INVALID_COUPON_NAME_LENGTH);
        }
    }

    private void validateDiscountRate(final int discountRate) {
        if (discountRate < 5 || discountRate > 90) {
            throw new BadRequestException(INVALID_COUPON_DISCOUNT_RATE);
        }
    }

    private void validatePeriod(final int period) {
        if (period < 1 || period > 365) {
            throw new BadRequestException(INVALID_COUPON_PERIOD_LENGTH);
        }
    }

    private void validateExpiredAt(final LocalDateTime expiredAt) {
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new BadRequestException(INVALID_COUPON_EXPIRATION_DATE);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public int getPeriod() {
        return period;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
