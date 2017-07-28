import org.junit.Assert;
import org.junit.Test;

/**
 * Created by johnny on 2017/7/13.
 */
public class TestJunit2 {
    String message = "Robert";
    MessageUtil messageUtil = new MessageUtil(message);

    @Test
    public void testSalutaionMessage() {
        System.out.println("Inside testSalutationMessage()");
        message = "Hi!" + "Robert";
        Assert.assertEquals(message, messageUtil.salutationMessage());
    }
}
