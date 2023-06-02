package cart.domain.coupon;

import java.time.LocalDateTime;

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
            throw new IllegalArgumentException("쿠폰 이름은 1글자 이상 50글자 이하로 입력해주세요.");
        }
    }

    private void validateDiscountRate(final int discountRate) {
        if (discountRate < 5 || discountRate > 90) {
            throw new IllegalArgumentException("쿠폰 할인율은 5% 이상 90% 이하로 입력해주세요");
        }
    }

    private void validatePeriod(final int period) {
        if (period < 1 || period > 365) {
            throw new IllegalArgumentException("쿠폰 기간은 1일 이상 365일 이하로 입력해주세요.");
        }
    }

    private void validateExpiredAt(final LocalDateTime expiredAt) {
        if (expiredAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("쿠폰 만료 날짜는 현재 시간 이후로 입력해주세요.");
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
