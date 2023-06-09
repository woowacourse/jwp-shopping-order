package shop.exception;

public class DatabaseException extends RuntimeException {

    private DatabaseException(String message) {
        super(message);
    }

    public static class DataConflictException extends DatabaseException {
        public DataConflictException(String message) {
            super(message);
        }
    }

    public static class IllegalDataException extends DatabaseException {
        public IllegalDataException(String message) {
            super(message);
        }
    }
}
