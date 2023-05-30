package cart.dto;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.Snippet;

public enum RequestSnippets {

    PRODUCT(ProductRequest.class, () -> Map.of(
            "name", "제품 명",
            "price", "제품 가격",
            "imageUrl", "제품 이미지 url"
    )),
    CART_ITEM(CartItemRequest.class, () -> Map.of(
            "productId", "제품 id"
    )),
    CART_ITEM_QUANTITY_UPDATE(CartItemQuantityUpdateRequest.class, () -> Map.of(
            "quantity", "수량"
    )),
    PRODUCT_WITH_ID(ProductRequest.WithId.class, () -> Map.of(
            "id", "제품 id",
            "name", "제품 명",
            "price", "제품 가격",
            "imageUrl", "제품 이미지 url"
    )),
    ORDER(OrderRequest.class, () -> join(
            Map.of(
                    "cartItemId", "장바구니 항목 id",
                    "product", "제품",
                    "quantity", "수량",
                    "couponIds", "쿠폰 id(들)"
            ),
            withPrefix("product.", PRODUCT_WITH_ID.fieldsSupplier.get())
    ));

    private static final String EMPTY = "";
    private static final String LIST_PREFIX = "[].";

    private final Class<?> clazz;
    private final Supplier<Map<String, String>> fieldsSupplier;

    RequestSnippets(Class<?> clazz, Supplier<Map<String, String>> fieldsSupplier) {
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
        return requestFields(toFields(withPrefix(getPrefixOf(dto), fieldsSupplier.get())));
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
