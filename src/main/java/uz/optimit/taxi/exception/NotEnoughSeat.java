package uz.optimit.taxi.exception;

public class NotEnoughSeat extends RuntimeException {
    public NotEnoughSeat(String message) {
        super(message);
    }
}
