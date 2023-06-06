package cart.ui.pageable;

import cart.exception.InvalidPageException;

public class Page {

    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_SIZE = "10";
    public static final Page DEFAULT = new Page(DEFAULT_PAGE, DEFAULT_SIZE);

    private final int page;
    private final int size;

    public Page(final int page, final int size) {
        validate(page, size);
        this.page = page;
        this.size = size;
    }

    public Page(final String rawPage, final String rawSize) {
        try {
            final int page = Integer.parseInt(rawPage);
            final int size = Integer.parseInt(rawSize);
            validate(page, size);
            this.page = page;
            this.size = size;
        } catch (NumberFormatException e) {
            throw new InvalidPageException("page 와 size 는 숫자여야 합니다.");
        }
    }

    private void validate(final int page, final int size) {
        if (page <= 0) {
            throw new InvalidPageException("page 번호는 0 이하일 수 없습니다.");
        }
        if (size <= 0) {
            throw new InvalidPageException("page의 size는 0 이하일 수 없습니다.");
        }
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return getOffset() + size;
    }

}
