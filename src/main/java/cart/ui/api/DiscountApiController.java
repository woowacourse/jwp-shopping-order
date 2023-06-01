package cart.ui.api;

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

    @GetMapping
    public ResponseEntity<List<DiscountInformationResponse>> getDiscountRate(final DiscountInformationRequest request) {
        return ResponseEntity.ok().build();
    }
}
