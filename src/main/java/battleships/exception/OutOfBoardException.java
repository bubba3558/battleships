package battleships.exception;

public class OutOfBoardException extends Exception {
    public OutOfBoardException(String message) {
        super(message);
    }
}
