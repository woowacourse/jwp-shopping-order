package cart.controller;

import cart.dto.DeliveryPolicyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery-policy")
public class DeliveryPolicyController {

    @GetMapping
    public ResponseEntity<DeliveryPolicyResponse> getDeliveryPolicy() {
        DeliveryPolicyResponse deliveryPolicy = new DeliveryPolicyResponse(3000, 30000);
        return ResponseEntity.ok().body(deliveryPolicy);
    }

}
