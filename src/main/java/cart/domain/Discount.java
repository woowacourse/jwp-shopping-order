package cart.domain;

import cart.exception.IllegalDiscountException;

public class Discount {

    private final Integer originPrice;
    private final Integer discountPrice;
    private final Integer priceAfterDiscount;

    private Discount(final Integer originPrice,
                     final Integer discountPrice,
                     final Integer priceAfterDiscount) {
        this.originPrice = originPrice;
        this.discountPrice = discountPrice;
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public static Discount of(Integer originPrice, Integer priceAfterDiscount) {
        validate(originPrice, priceAfterDiscount);

        return new Discount(originPrice, priceAfterDiscount - originPrice, priceAfterDiscount);
    }

    private static void validate(Integer originPrice, Integer priceAfterDiscount) {
        if (originPrice < priceAfterDiscount) {
            throw new IllegalDiscountException("할인 정책으로 금액을 추가할 수 없습니다.");
        }
    }

    public Integer getOriginPrice() {
        return originPrice;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public Integer getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "originPrice=" + originPrice +
                ", discountPrice=" + discountPrice +
                ", priceAfterDiscount=" + priceAfterDiscount +
                '}';
    }

}
