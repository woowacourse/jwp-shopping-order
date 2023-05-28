package cart.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponApiController {

    @GetMapping
    public ResponseEntity<List<CouponResponse>> findCoupons() {
        return ResponseEntity.ok(List.of(new CouponResponse("레오이용권", 100, 0)));
    }
}
