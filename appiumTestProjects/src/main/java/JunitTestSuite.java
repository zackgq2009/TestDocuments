import junit.framework.*;
public class JunitTestSuite {
    public static void main(String[] a) {
        // add the test's in the suite
        TestSuite suite = new TestSuite(TestJunit.class );
        TestResult result = new TestResult();
        System.out.println(suite.countTestCases());
        suite.run(result);
        System.out.println("Number of test cases = " + result.runCount());
    }
}