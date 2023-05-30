package cart.ui;

import cart.application.OrderService;
import cart.dto.OrderCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //상품 등록
    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody OrderCreateRequest orderCreateRequest){
        Long id = orderService.createOrder(orderCreateRequest);
        return ResponseEntity.created(URI.create("/orders" + id)).build();
    }
}
