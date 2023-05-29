package cart.application;

import org.springframework.stereotype.Service;

import cart.domain.Member;
import cart.domain.discount.DiscountPolicy;
import cart.dto.TotalDiscountInfoResponse;

@Service
public class PriceService {
    private final DiscountPolicy discountPolicy;

    public PriceService(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public TotalDiscountInfoResponse getDiscountInformation(Integer price, Member member) {
        return null;
    }

}
