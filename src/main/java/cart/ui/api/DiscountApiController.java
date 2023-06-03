package cart.ui.api;

import cart.application.DiscountService;
import cart.dto.response.DiscountResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discount")
public class DiscountApiController {

    private final DiscountService discountService;

    public DiscountApiController(final DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping
    public ResponseEntity<List<DiscountResponse>> getDiscount(@RequestParam("price") final int price, @RequestParam("memberGrade") final String grade) {
        final List<DiscountResponse> discountResponses = discountService.getDiscount(price, grade);
        return ResponseEntity.ok(discountResponses);
    }
}
