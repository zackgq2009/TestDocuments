import com.sun.tools.javah.JNI;
import junit_testworkspace.JunitAnnotation;
import junit_testworkspace.TestAssertions;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
/**
 * Created by johnny on 2017/7/12.
 */
public class TestRunner2 {
    public static void main(String[] args) {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        Result result = junit.run(PrimeNumberCheckerTest.class);
//        Result result = (new JUnitCore()).run(TestAssertions.class);
        System.out.println(result.getFailureCount());
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}
