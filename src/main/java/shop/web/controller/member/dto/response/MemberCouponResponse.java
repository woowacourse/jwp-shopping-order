package shop.web.controller.member.dto.response;

import shop.application.member.dto.MemberCouponDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MemberCouponResponse {
    private Long id;
    private String name;
    private Integer discountRate;
    private LocalDateTime expiredAt;
    private Boolean isUsed;

    private MemberCouponResponse() {
    }

    private MemberCouponResponse(Long id, String name, Integer discountRate,
                                 LocalDateTime expiredAt, Boolean isUsed) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
        this.expiredAt = expiredAt;
        this.isUsed = isUsed;
    }

    public static MemberCouponResponse of(MemberCouponDto memberCouponDto) {
        return new MemberCouponResponse(
                memberCouponDto.getId(),
                memberCouponDto.getName(),
                memberCouponDto.getDiscountRate(),
                memberCouponDto.getExpiredAt(),
                memberCouponDto.isUsed()
        );
    }

    public static List<MemberCouponResponse> of(List<MemberCouponDto> memberCouponDtos) {
        return memberCouponDtos.stream()
                .map(MemberCouponResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }
}
