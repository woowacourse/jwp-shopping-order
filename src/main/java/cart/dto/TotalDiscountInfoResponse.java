package cart.dto;

import java.util.List;
import java.util.stream.Collectors;

import cart.domain.price.discount.DiscountInformation;

public class TotalDiscountInfoResponse {
    private final List<DiscountInfoResponse> discountInfoResponses;

    private TotalDiscountInfoResponse(List<DiscountInfoResponse> discountInfoResponses) {
        this.discountInfoResponses = discountInfoResponses;
    }

    public static TotalDiscountInfoResponse of(List<DiscountInformation> discountInformations) {
        final List<DiscountInfoResponse> discountInfoResponses = discountInformations.stream()
                .map(DiscountInfoResponse::of)
                .collect(Collectors.toUnmodifiableList());
        return new TotalDiscountInfoResponse(discountInfoResponses);
    }

    public List<DiscountInfoResponse> getDiscountInfoResponses() {
        return discountInfoResponses;
    }
}
