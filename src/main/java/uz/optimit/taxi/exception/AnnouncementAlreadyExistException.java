package uz.optimit.taxi.exception;

public class AnnouncementAlreadyExistException extends RuntimeException{
    public AnnouncementAlreadyExistException(String name){
        super(name);
    }
}
