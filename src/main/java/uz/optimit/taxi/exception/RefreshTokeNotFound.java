package uz.optimit.taxi.exception;

public class RefreshTokeNotFound extends RuntimeException {
    public RefreshTokeNotFound(String reFreshTokenNotFound) {
        super(reFreshTokenNotFound);
    }
}
