package cart.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
public class PointApiController {

    @GetMapping
    public ResponseEntity<PointResponse> findPointByMember() {
        return ResponseEntity.ok(new PointResponse(1000));
    }
}
