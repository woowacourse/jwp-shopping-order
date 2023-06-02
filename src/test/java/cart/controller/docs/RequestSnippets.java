package cart.controller.docs;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.DiscountPolicyRequest;
import cart.dto.MemberCouponRequest;
import cart.dto.OrderItemRequest;
import cart.dto.OrderRequest;
import cart.dto.ProductRequest;

public enum RequestSnippets {

    PRODUCT(ProductRequest.class, () -> List.of(
            fieldWithPath("name").description("제품 명"),
            fieldWithPath("price").description("제품 가격"),
            fieldWithPath("imageUrl").description("제품 이미지 url")
    )),
    CART_ITEM(CartItemRequest.class, () -> List.of(
            fieldWithPath("productId").description("제품 id")
    )),
    CART_ITEM_QUANTITY_UPDATE(CartItemQuantityUpdateRequest.class, () -> List.of(
            fieldWithPath("quantity").description("수량")
    )),
    PRODUCT_WITH_ID(ProductRequest.WithId.class, () -> List.of(
            fieldWithPath("id").description("제품 id"),
            fieldWithPath("name").description("제품 명"),
            fieldWithPath("price").description("제품 가격"),
            fieldWithPath("imageUrl").description("제품 이미지 url")
    )),
    DISCOUNT_POLICY(DiscountPolicyRequest.class, () -> List.of(
            fieldWithPath("type").description("할인 종류").type(JsonFieldType.STRING),
            fieldWithPath("amount").description("할인량").type(JsonFieldType.NUMBER)
    )),
    MEMBER_COUPON(MemberCouponRequest.class, () -> join(List.of(
                    fieldWithPath("couponId").description("쿠폰 id").type(JsonFieldType.NUMBER),
                    fieldWithPath("name").description("쿠폰 이름").type(JsonFieldType.STRING),
                    fieldWithPath("discount").description("할인 정보").type(JsonFieldType.OBJECT)
            ),
            withPrefix("discount.", DISCOUNT_POLICY.fieldsSupplier.get())
    )),
    ORDER_ITEM(OrderItemRequest.class, () -> join(join(List.of(
                            fieldWithPath("id").description("장바구니 항목 id"),
                            fieldWithPath("product").description("제품"),
                            fieldWithPath("quantity").description("수량"),
                            fieldWithPath("coupons").description("사용할 쿠폰(들)")
                    ),
                    withPrefix("product.", PRODUCT_WITH_ID.fieldsSupplier.get())),
            asOptional(withPrefix("coupons[].", MEMBER_COUPON.fieldsSupplier.get()))
    )),
    ORDER(OrderRequest.class, () -> join(List.of(
                    fieldWithPath("deliveryFee").description("배송비"),
                    fieldWithPath("orderItems").description("주문할 장바구니 항목들")
            ),
            withPrefix("orderItems[].", ORDER_ITEM.fieldsSupplier.get())
    ));

    private static final String EMPTY = "";
    private static final String LIST_PREFIX = "[].";

    private final Class<?> clazz;
    private final Supplier<List<FieldDescriptor>> fieldsSupplier;

    RequestSnippets(Class<?> clazz, Supplier<List<FieldDescriptor>> fieldsSupplier) {
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
        return requestFields(withPrefix(getPrefixOf(dto), fieldsSupplier.get()));
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

    private static List<FieldDescriptor> asOptional(List<FieldDescriptor> fields) {
        return fields.stream()
                .map(FieldDescriptor::optional)
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
