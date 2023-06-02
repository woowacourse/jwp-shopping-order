package cart.ui;

import cart.service.PointService;
import cart.domain.Member;
import cart.dto.PointResponse;
import cart.dto.SavingPointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointController {
    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/points")
    public ResponseEntity<PointResponse> getUserPoints(Member member) {
        PointResponse pointResponse = pointService.findUserPoints(member);
        return ResponseEntity.ok().body(pointResponse);
    }

    @GetMapping("/saving-point")
    public ResponseEntity<SavingPointResponse> getSavingPoints(@RequestParam("totalPrice") Long totalPrice) {
        SavingPointResponse savingPointResponse = pointService.findSavingPoints(totalPrice);
        return ResponseEntity.ok().body(savingPointResponse);
    }
}
