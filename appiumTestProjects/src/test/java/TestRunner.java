import junit_testworkspace.TestEmployeeDetails;
import junit_testworkspace.JunitAnnotation;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Created by johnny on 2017/7/11.
 */
public class TestRunner {
    public static  void main(String[] args) {
        Result result = JUnitCore.runClasses(PrimeNumberCheckerTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}
