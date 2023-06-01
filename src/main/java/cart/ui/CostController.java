package cart.ui;

import cart.application.CartItemService;
import cart.application.OrderService;
import cart.dao.OrderDao;
import cart.domain.Member;
import cart.dto.CostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/costs")
public class CostController {

    @Autowired
    private CartItemService cartItemService;

    @GetMapping
    public ResponseEntity<CostResponse> getCosts(Member member){
        return ResponseEntity.ok(cartItemService.getCosts(member.getId()));
    }
}
