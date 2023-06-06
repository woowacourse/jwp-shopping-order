package cart.exception;

public class NoSuchDataExistException extends RuntimeException {
    private final String data;
    private final String dataId;

    public NoSuchDataExistException(final String data, final String dataId) {
        this.data = data;
        this.dataId = dataId;
    }

    public String getData() {
        return data;
    }

    public String getDataId() {
        return dataId;
    }
}
