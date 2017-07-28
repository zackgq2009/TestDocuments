import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by johnny on 2017/7/13.
 */
//@Ignore
public class TestJunit {
    String message = "Robert";
    MessageUtil messageUtil = new MessageUtil(message);

//    @Ignore
    @Test (expected = ArithmeticException.class)
    public void testPrintMessage() {
        System.out.println("Inside testPrintMessage");
//        messageUtil.printMessage();
        assertEquals(message, messageUtil.printMessage());
    }

//    @Ignore
    @Test
    public void testSalutationMessage() {
        System.out.println("Inside testSalutationMessage()");
        message = "Hi!" + "Robert";
        assertEquals(message, messageUtil.salutationMessage());
    }
}
