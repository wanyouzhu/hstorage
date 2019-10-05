package ltd.highsoft.framework.hstore;

public class MalformedDataException extends RuntimeException {

    public MalformedDataException(String message) {
        super(message);
    }

    public MalformedDataException(String message, Throwable cause) {
        super(message, cause);
    }

}
