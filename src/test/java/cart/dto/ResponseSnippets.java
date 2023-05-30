package cart.dto;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.Snippet;

public enum ResponseSnippets {
    PRODUCT(ProductResponse.class, () -> Map.of(
            "id", "제품 id",
            "name", "제품 명",
            "price", "제품 가격",
            "imageUrl", "제품 이미지 url"
    )),
    CART_ITEM(CartItemResponse.class, () -> join(
            Map.of(
                    "id", "카트 id",
                    "quantity", "수량",
                    "product", "제품 정보"
            ),
            withPrefix("product.", PRODUCT.fieldsSupplier.get())
    )),
    ORDER_ITEM(OrderItemResponse.class, () -> Map.of(
            "orderItemId", "주문 항목 id",
            "product", "주문 시점의 상품",
            "total", "주문 항목의 총 금액",
            "quantity", "주문 항목의 수량"
    )),
    ORDER(OrderResponse.class, () -> join(join(
                    Map.of(
                            "orderId", "주문 id",
                            "orderItems", "주문한 항목들"
                    ),
                    withPrefix("orderItems[].", ORDER_ITEM.fieldsSupplier.get())),
            withPrefix("orderItems[].product.", PRODUCT.fieldsSupplier.get())
    ));

    private static final String EMPTY = "";
    private static final String LIST_PREFIX = "[].";

    private final Class<?> clazz;
    private final Supplier<Map<String, String>> fieldsSupplier;

    ResponseSnippets(Class<?> clazz, Supplier<Map<String, String>> fieldsSupplier) {
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
        return responseFields(toFields(withPrefix(getPrefixOf(dto), fieldsSupplier.get())));
    }

    private String getNameOf(Object dto) {
        String name = clazz.getSimpleName();
        if (isList(dto)) {
            name += "s";
        }
        return name;
    }

    private static List<FieldDescriptor> toFields(Map<String, String> fields) {
        return fields.entrySet().stream()
                .map(entry -> fieldWithPath(entry.getKey()).description(entry.getValue()))
                .collect(Collectors.toList());
    }

    private static Map<String, String> withPrefix(String prefix, Map<String, String> fields) {
        return fields.entrySet().stream()
                .map(entry -> Map.entry(prefix + entry.getKey(), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static Map<String, String> join(Map<String, String> fields, Map<String, String> otherFields) {
        HashMap<String, String> newFields = new HashMap<>(fields);
        newFields.putAll(otherFields);
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
