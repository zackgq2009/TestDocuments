import org.junit.*;
import org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by johnny on 2017/7/13.
 */
@RunWith(Parameterized.class)
public class PrimeNumberCheckerTest {
    private Integer inputNumber;
    private Boolean expectedResult;
    private PrimeNumberChecker primeNumberChecker;

    //Each parameter should be placed as an argument here
    //Every time runner triggers, it will pass the arguments
    //from parameters we defined in primeNumbers() method
    public PrimeNumberCheckerTest(Integer inputNumber, Boolean expectedResult) {
        this.inputNumber = inputNumber;
        this.expectedResult = expectedResult;
    }

    @Before
    public void initialize() {
        primeNumberChecker = new PrimeNumberChecker();
    }

    @Parameters
    public static Collection primeNumbers() {
        return Arrays.asList(new Object[][]{
                {2, true},
                {6, false},
                {19, true},
                {22, false},
                {23, true}
        });
    }

    //This test will run 5 times since we have 5 parameters defined
    @Test
    public void testPrimeNumberChecker() {
        System.out.println("Parameterized  Number is :" + inputNumber);
        Assert.assertEquals(expectedResult, primeNumberChecker.validate(inputNumber));
    }

}
