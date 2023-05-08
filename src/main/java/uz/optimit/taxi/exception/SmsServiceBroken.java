package uz.optimit.taxi.exception;

public class SmsServiceBroken extends RuntimeException {
  public SmsServiceBroken(String s){
        super(s);
    }
}
