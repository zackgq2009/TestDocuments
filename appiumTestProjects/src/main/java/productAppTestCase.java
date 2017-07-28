import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.io.File;
import java.util.Set;

public class productAppTestCase {
    private static AndroidDriver productAppSession = null;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        //我们的参数都可以通过JSON配置到DesiredCapabilities中
//        File classpathRoot = new File(System.getProperty("user.dir"));
//        File appDir = new File(classpathRoot, "../../../");
//        File app = new File(appDir.getCanonicalPath(), "123456guoqingRelease-1.0-1.apk");
//        capabilities.setCapability("app", app.getAbsolutePath());
        capabilities.setCapability("app", "/Users/johnny/Desktop/123456guoqingRelease-1.0-1.apk");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("udid", "192.168.58.101:5555");
        capabilities.setCapability("platformVersion", "5.1");

        URL url = new URL("http://127.0.0.1:4723/wd/hub");

        productAppSession = new AndroidDriver(url, capabilities);
//            productAppSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() throws Exception {
//        productAppSession.quit();
        Boolean isInstalled = productAppSession.isAppInstalled("com.maxleap.mobile.appmaker");
        System.out.println(isInstalled);
        productAppSession.runAppInBackground(3);//应用运行至后台三秒钟，三秒之后应用会自动运行到前台
        productAppSession.closeApp();//应用直接关闭，应该不是后台运行
        //startActivity的时候，先填写package,然后填写appActivity
        productAppSession.startActivity("com.maxleap.mobile.appmaker","com.maxwon.mobile.appmaker.activities.LaunchActivity", null, null);
    }

    @Test
    public void startTest() throws Exception {
//        productAppSession.startActivity("com.maxleap.mobile.appmaker","launchActivity",null,null);
        Set<String> contextNames = productAppSession.getContextHandles();
        for (String contextName : contextNames) {
            System.out.println(contextName);
        }
//        WebElement element = productAppSession.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.view.View[1]/android.widget.TextView[1]");

//        productAppSession.context(contextNames.toArray());

    }
}