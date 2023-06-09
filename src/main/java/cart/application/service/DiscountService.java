package cart.application.service;

import cart.application.domain.Grade;
import cart.application.domain.discount.GradeDiscount;
import cart.application.domain.discount.PriceDiscount;
import cart.ui.dto.response.DiscountResponse;
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
