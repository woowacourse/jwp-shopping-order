package cart.ui;

import cart.ui.pageable.Page;
import cart.ui.pageable.Pageable;
import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PageArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Pageable.class)
                && parameter.getParameterType().isAssignableFrom(Page.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory)
            throws Exception {
        try {
            String page = webRequest.getParameter("page");
            if (Objects.isNull(page) || Integer.parseInt(page) <= 0) {
                page = Page.DEFAULT_PAGE;
            }
            String size = webRequest.getParameter("size");
            if (Objects.isNull(size) || Integer.parseInt(size) <= 0) {
                size = Page.DEFAULT_SIZE;
            }
            return new Page(Integer.parseInt(page), Integer.parseInt(size));

        } catch (NullPointerException | NumberFormatException e) {
            throw new MethodArgumentNotValidException(
                    parameter,
                    new BeanPropertyBindingResult(parameter, "parameter : page")
            );
        }
    }
}
