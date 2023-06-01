package cart.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class CouponInfo {
    private final Long id;
    private final String name;
    private final Integer minPrice;
    private final Integer maxPrice;
    private final LocalDateTime expiredAt;

    public CouponInfo(final Long id, final String name, final Integer minPrice, final Integer maxPrice, final LocalDateTime expiredAt) {
        this.id = id;
        this.name = name;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.expiredAt = expiredAt;
    }

    public boolean isNotExpired() {
        return expiredAt.isAfter(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CouponInfo that = (CouponInfo) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(minPrice, that.minPrice)
                && Objects.equals(maxPrice, that.maxPrice)
                && Objects.equals(expiredAt, that.expiredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, minPrice, maxPrice, expiredAt);
    }

    @Override
    public String toString() {
        return "CouponInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", expiredAt=" + expiredAt +
                '}';
    }
}
