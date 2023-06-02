package cart.ui.api;

import cart.application.DiscountService;
import cart.dto.DiscountInformationRequest;
import cart.dto.DiscountInformationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/discount")
public final class DiscountApiController {

    private final DiscountService discountService;

    public DiscountApiController(final DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping
    public ResponseEntity<List<DiscountInformationResponse>> getDiscountRate(final DiscountInformationRequest request) {
        final List<DiscountInformationResponse> result = discountService.getDiscountInfo(request.getMemberGrade(), request.getPrice());

        return ResponseEntity.ok(result);
    }
}
