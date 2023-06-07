package cart.ui.api;

import cart.application.DiscountService;
import cart.dto.DiscountRateRequest;
import cart.dto.DiscountResponse;
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
    public ResponseEntity<List<DiscountResponse>> getDiscountRate(final DiscountRateRequest request) {
        final List<DiscountResponse> result = discountService.getDiscountInfo(request.getMemberGrade(), request.getPrice());

        return ResponseEntity.ok(result);
    }
}
