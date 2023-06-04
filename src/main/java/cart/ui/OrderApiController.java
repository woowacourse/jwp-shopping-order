package cart.ui;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.request.MemberCouponAddRequest;
import cart.dto.request.OrderRequest;
import cart.dto.response.CouponsResponse;
import cart.dto.response.ExceptionResponse;
import cart.dto.response.MemberCouponsResponse;
import cart.dto.response.OrderDetailResponse;
import cart.dto.response.OrdersResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "회원 모든 주문 내역 조회", description = "회원의 모든 주문 내역을 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "회원 모든 주문 내역 조회 성공"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        )
    })
    @GetMapping
    public ResponseEntity<OrdersResponse> getAllOrdersOfMember(Member member) {
        OrdersResponse ordersResponse = orderService.getOrdersOf(member);
        return ResponseEntity.ok().body(ordersResponse);
    }

    @Operation(summary = "회원 주문 내역 조회", description = "회원의 주문 내역을 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "회원 주문 내역 조회 성공"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "회원 주문 내역 조회 실패",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "권한 없음",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        )
    })
    @GetMapping("{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrder(Member member, @PathVariable Long orderId) {
        OrderDetailResponse orderDetailResponse = orderService.getOrderOf(member, orderId);
        return ResponseEntity.ok().body(orderDetailResponse);
    }

    @Operation(summary = "주문 추가", description = "회원의 장바구니 목록을 통하여 주문한다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "주문 추가 성공"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "주문 추가 실패",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "권한 없음",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        )
    })
    @PostMapping
    public ResponseEntity<Void> addOrder(Member member, @RequestBody OrderRequest orderRequest) {
        Long orderId = orderService.add(member, orderRequest);
        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @Operation(summary = "주문 삭제", description = "주문을 취소한다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "주문 추가 성공"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "주문 삭제 실패",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "권한 없음",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        )
    })
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(Member member, @PathVariable Long orderId) {
        orderService.deleteOrder(member, orderId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "선택 물품에 대한 회원 쿠폰 정보 조회", description = "선택한 장바구니 물품들에 대한 회원 쿠폰 정보를 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "회원 쿠폰 정보 조회 성공"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "회원 쿠폰 정보 조회 실패",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "권한 없음",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        )
    })
    @GetMapping("/coupons")
    public ResponseEntity<MemberCouponsResponse> getMemberCouponsByItems(Member member,
        @RequestParam("cartItemId") List<Long> cartItemIds) {
        MemberCouponsResponse memberCouponsResponse = orderService.getMemberCoupons(member, cartItemIds);
        return ResponseEntity.ok().body(memberCouponsResponse);
    }

    @Operation(summary = "발급 가능한 쿠폰 조회", description = "발급 가능한 쿠폰 리스트를 조회한다.")
    @ApiResponse(
        responseCode = "200",
        description = "쿠폰 조회 성공"
    )
    @GetMapping("/coupons")
    public ResponseEntity<CouponsResponse> getAllCoupons(Member member) {
        CouponsResponse couponsResponse = orderService.getAllCoupons();
        return ResponseEntity.ok().body(couponsResponse);
    }

    @Operation(summary = "쿠폰 발급", description = "회원이 쿠폰을 발급한다")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "쿠폰 발급 성공"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "쿠폰 발급 실패",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
        )
    })
    @GetMapping("/coupons/{couponId}")
    public ResponseEntity<Void> addMemberCoupon(Member member, @PathVariable Long couponId, @Valid
        MemberCouponAddRequest memberCouponAddRequest) {
        Long memberCouponId = orderService.addMemberCoupon(member, couponId, memberCouponAddRequest);
        return ResponseEntity.created(URI.create("/coupons/member/" + memberCouponId)).build();
    }
}
