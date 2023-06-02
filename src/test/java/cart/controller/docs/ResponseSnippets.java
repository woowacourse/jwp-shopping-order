package cart.controller.docs;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.Snippet;

import cart.dto.CartItemResponse;
import cart.dto.DiscountPolicyResponse;
import cart.dto.MemberCouponResponse;
import cart.dto.OrderItemResponse;
import cart.dto.OrderResponse;
import cart.dto.ProductResponse;

public enum ResponseSnippets {
    PRODUCT(ProductResponse.class, () -> List.of(
            fieldWithPath("id").description("제품 id"),
            fieldWithPath("name").description("제품 명"),
            fieldWithPath("price").description("제품 가격"),
            fieldWithPath("imageUrl").description("제품 이미지 url")
    )),
    CART_ITEM(CartItemResponse.class, () -> join(List.of(
                    fieldWithPath("id").description("카트 id"),
                    fieldWithPath("quantity").description("수량"),
                    fieldWithPath("product").description("제품 정보")
            ),
            withPrefix("product.", PRODUCT.fieldsSupplier.get())
    )),
    DISCOUNT_POLICY(DiscountPolicyResponse.class, () -> List.of(
            fieldWithPath("type").description("할인 종류"),
            fieldWithPath("amount").description("할인량")
    )),
    MEMBER_COUPON(MemberCouponResponse.class, () -> join(List.of(
                    fieldWithPath("couponId").description("쿠폰 id"),
                    fieldWithPath("name").description("쿠폰 이름"),
                    fieldWithPath("discount").description("할인 정보")
            ),
            withPrefix("discount.", DISCOUNT_POLICY.fieldsSupplier.get())
    )),
    ORDER_ITEM(OrderItemResponse.class, () -> join(join(List.of(
                            fieldWithPath("orderItemId").description("주문 항목 id"),
                            fieldWithPath("product").description("주문 시점의 상품 정보"),
                            fieldWithPath("total").description("최종 결제 금액"),
                            fieldWithPath("quantity").description("주문 수량"),
                            fieldWithPath("coupons").description("사용한 쿠폰들")
                    ),
                    withPrefix("product.", PRODUCT.fieldsSupplier.get())),
            withPrefix("coupons[].", MEMBER_COUPON.fieldsSupplier.get())
    )),
    ORDER(OrderResponse.class, () -> join(List.of(
                    fieldWithPath("orderId").description("주문 id"),
                    fieldWithPath("deliveryFee").description("배송비"),
                    fieldWithPath("total").description("최종 결제 금액"),
                    fieldWithPath("orderItems").description("주문한 항목들")
            ),
            withPrefix("orderItems[].", ORDER_ITEM.fieldsSupplier.get())
    ));

    private static final String EMPTY = "";
    private static final String LIST_PREFIX = "[].";

    private final Class<?> clazz;
    private final Supplier<List<FieldDescriptor>> fieldsSupplier;

    ResponseSnippets(Class<?> clazz, Supplier<List<FieldDescriptor>> fieldsSupplier) {
        this.clazz = clazz;
        this.fieldsSupplier = fieldsSupplier;
    }

    public static Optional<Snippet> of(Object dto) {
        return Arrays.stream(values())
                .filter(that -> that.clazz.equals(getClassOf(dto)))
                .map(it -> it.toSnippet(dto))
                .findAny();
    }

    private Snippet toSnippet(Object dto) {
        return responseFields(withPrefix(getPrefixOf(dto), fieldsSupplier.get()));
    }

    private static List<FieldDescriptor> withPrefix(String prefix, List<FieldDescriptor> fields) {
        return fields.stream()
                .map(it -> {
                    FieldDescriptor descriptor = fieldWithPath(prefix + it.getPath())
                            .description(it.getDescription())
                            .type(it.getType());
                    if (it.isOptional()) {
                        return descriptor.optional();
                    }
                    return descriptor;
                })
                .collect(Collectors.toList());
    }

    private static List<FieldDescriptor> join(List<FieldDescriptor> fields, List<FieldDescriptor> otherFields) {
        List<FieldDescriptor> newFields = new ArrayList<>(fields);
        newFields.addAll(otherFields);
        return newFields;
    }

    private static <T> Class<?> getClassOf(T dto) {
        if (dto instanceof List) {
            return ((List<?>)dto).stream()
                    .findFirst()
                    .map(Object::getClass)
                    .orElseThrow(() -> new IllegalArgumentException("빈 리스트입니다"));
        }
        return dto.getClass();
    }

    private static <T> String getPrefixOf(T dto) {
        if (isList(dto)) {
            return LIST_PREFIX;
        }
        return EMPTY;
    }

    private static <T> boolean isList(T dto) {
        return dto instanceof List;
    }
}
