package junit_workspace;

/**
 * Created by johnny on 2017/7/13.
 */
public class MessageUtil {
    private String message;

    //Constructor
    //@param message to be printed
    public MessageUtil(String message) {
        this.message = message;
    }

    //prints the message
    public String printMessage() {
        System.out.println(message);
        return message;
    }
}
