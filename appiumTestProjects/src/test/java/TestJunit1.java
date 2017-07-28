import org.junit.Test;
import org.junit.Assert.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by johnny on 2017/7/13.
 */
public class TestJunit1 {

    String message = "Robert";
    MessageUtil messageUtil = new MessageUtil(message);

    @Test
    public void testPrintMessage() {
        System.out.println("Inside testPrintMessage()");
        assertEquals(message, messageUtil.printMessage());
    }
}
