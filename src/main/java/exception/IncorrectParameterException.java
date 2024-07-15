package exception;

public class IncorrectParameterException extends RuntimeException{
    public IncorrectParameterException (String msg){
        super(msg);
    }
}
