package junit_testworkspace;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * Created by johnny on 2017/7/12.
 */
public class TestAssertions {
    private static String str1, str2, str3, str4, str5;
    private static int val1, val2;
    private static String expectedArray[];
    private static String resultArray[];

    @BeforeClass
    public static void setUp() {
        //test data
        str1 = new String("abc");
        str2 = new String("abc");
        str3 = null;
        str4 = "abc";
        str5 = "abc";
        val1 = 5;
        val2 = 6;
        expectedArray = new String[]{"one", "two", "three"};
        resultArray = new String[]{"one", "two", "three"};
    }

    @Test
    public void testAssertEquals() {
        assertEquals(str1, str3);
    }

    @Test
    public void testAssertTrue() {
        assertTrue(val1 > val2);
    }

    @Test
    public void testAssertFalse() {
        assertFalse(val1 < val2);
    }

    @Test
    public void testAssertNotNull() {
        assertNotNull(str3);
    }

    @Test
    public void testAssertNull() {
        assertNull(str1);
    }

    @Test
    public void testAssertSame() {
        assertSame(str4, str5);
        assertSame(str1, str2);
    }

    @Test
    public void testAssertNotSame() {
        assertNotSame(str1, str4);
    }

    @Test
    public void testAssertArrayEquals() {
        assertArrayEquals(expectedArray, resultArray);
    }

    @AfterClass
    public static void tearDown() {

    }
}
