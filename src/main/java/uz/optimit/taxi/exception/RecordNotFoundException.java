package uz.optimit.taxi.exception;

public class RecordNotFoundException extends RuntimeException{
    public RecordNotFoundException(String name){
        super(name);
    }
}
