package cart.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;

import cart.domain.price.discount.DiscountInformation;

public class TotalDiscountInfoResponse {
    private final List<DiscountInfoResponse> discountInformation;

    @JsonCreator
    private TotalDiscountInfoResponse(List<DiscountInfoResponse> discountInformation) {
        this.discountInformation = discountInformation;
    }

    public static TotalDiscountInfoResponse from(List<DiscountInformation> discountInformations) {
        final List<DiscountInfoResponse> discountInfoResponses = discountInformations.stream()
                .map(DiscountInfoResponse::from)
                .collect(Collectors.toUnmodifiableList());
        return new TotalDiscountInfoResponse(discountInfoResponses);
    }

    public List<DiscountInfoResponse> getDiscountInformation() {
        return discountInformation;
    }
}
