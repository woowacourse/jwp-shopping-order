package cart.application;

import cart.domain.Grade;
import cart.domain.discount.GradeDiscount;
import cart.domain.discount.PriceDiscount;
import cart.dto.response.DiscountResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {

    public List<DiscountResponse> getDiscount(final int price, final String gradeName) {
        return Stream.of(
                new GradeDiscount(Grade.from(gradeName), price), new PriceDiscount(price)
                ).map(DiscountResponse::from)
                .collect(Collectors.toList());
    }
}
