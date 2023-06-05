package cart.exception;

public class NoSuchDataExistException extends RuntimeException {
    private final String data;
    private final String dataId;

    public NoSuchDataExistException(final String data, final String dataId) {
        this.data = data;
        this.dataId = dataId;
    }

    @Override
    public String toString() {
        return "NoSuchDataExistException{" +
                "data='" + data + '\'' +
                ", dataId='" + dataId + '\'' +
                '}';
    }
}
