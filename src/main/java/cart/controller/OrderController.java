package cart.controller;

import cart.auth.Auth;
import cart.auth.Credential;
import cart.dto.OrderRequest;
import cart.exception.ExceptionResponse;
import cart.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@SecurityRequirement(name = "basicAuth")
@Tag(name = "주문", description = "장바구니의 상품을 주문한다")
@RequestMapping("/orders")
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "장바구니 상품 주문", description = "장바구니에 담긴 상품을 주문한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "장바구니 상품 주문 성공."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "장바구니 상품 주문 실패.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<Void> order(@Auth final Credential credential, @RequestBody final OrderRequest request) {
        final Long id = orderService.save(credential.getMemberId(), request);
        return ResponseEntity.created(URI.create("/orders/" + id)).build();
    }
}
