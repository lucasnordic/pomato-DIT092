package tool;

public class InvalidDataInput extends RuntimeException  {


    public InvalidDataInput(String message) throws RuntimeException {
        super(message);
    }

}