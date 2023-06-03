package cart.paging;

import cart.dto.PageRequest;
import cart.exception.PagingException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PagingArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(PageRequest.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final String page = webRequest.getParameter("page");
        final String size = webRequest.getParameter("size");

        if (page == null || page.isBlank()) {
            throw new PagingException.NullPage();
        }
        if (size == null || page.isBlank()) {
            throw new PagingException.NullSize();
        }

        return new PageRequest(Integer.valueOf(page), Integer.valueOf(size));
    }
}
