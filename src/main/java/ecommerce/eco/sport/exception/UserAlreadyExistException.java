package ecommerce.eco.sport.exception;

public class UserAlreadyExistException  extends RuntimeException{
    public UserAlreadyExistException(String message){
        super(message);
    }
}
