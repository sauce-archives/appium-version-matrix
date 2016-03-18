import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testobject.api.TestObjectClient;
import org.testobject.api.TestObjectRemoteClient;
import org.testobject.rest.api.DeviceDescriptor;

import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TestRandomDevice {

    private AppiumDriver driver;

    private final static String EXPECTED_RESULT_FOUR = "4";
    private final static String EXPECTED_RESULT_ERROR = "Error";

    @Before
    public void setUp() throws Exception, NoDeviceAvailableException {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("noSign", "true");
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");

        /* These are the capabilities we must provide to run our test on TestObject. */
        capabilities.setCapability("testobject_api_key", System.getenv("TESTOBJECT_API_KEY")); // API key through env variable

        String appId = System.getenv("TESTOBJECT_APP_ID") != null ? System.getenv("TESTOBJECT_APP_ID") : "1";
        capabilities.setCapability("testobject_app_id", appId);

        // Appium version is pulled from environment variables, so we can test multiple using Travis
        String appiumVersion = System.getenv("TESTOBJECT_APPIUM_VERSION") != null ? System.getenv("TESTOBJECT_APPIUM_VERSION") : "1.4.8";
        capabilities.setCapability("testobject_appium_version", appiumVersion);

        // Get a random available device to run our test on
        String device = getRandomDevice();
        capabilities.setCapability("testobject_device", device);

        capabilities.setCapability("testobject_test_name", "Appium Version Matrix with Random Device");

        String appiumEndpoint = System.getenv("TESTOBJECT_APPIUM_ENDPOINT") != null ? System.getenv("TESTOBJECT_APPIUM_ENDPOINT") : "https://app.testobject.com:443/api/appium/wd/hub";
        /* The driver will take care of establishing the connection, so we must provide
        * it with the correct endpoint and the requested capabilities. */
        driver = new AndroidDriver(new URL(appiumEndpoint), capabilities);

        System.out.println(driver.getCapabilities().getCapability("testobject_test_report_url"));
        System.out.println(driver.getCapabilities().getCapability("testobject_test_live_view_url"));
        System.out.println("Executing test on: " + device);
    }

    /* We disable the driver after EACH test has been executed. */
    @After
    public void tearDown(){
        if (driver != null) {
            driver.quit();
        }
    }


    /* A simple addition, it expects the correct result to appear in the result field. */
    @Test
    public void twoPlusTwoOperation() {

        MobileElement buttonTwo = (MobileElement)(driver.findElementById("digit2"));
        buttonTwo.click();

        MobileElement buttonPlus = (MobileElement)(driver.findElementById("plus"));
        buttonPlus.click();

        buttonTwo.click();

        MobileElement buttonEquals = (MobileElement)(driver.findElementById("equal"));
        buttonEquals.click();

        MobileElement resultField = (MobileElement)(driver.findElementByClassName("android.widget.EditText"));

        /* Check if within given time the correct result appears in the designated field. */
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.textToBePresentInElement(resultField, EXPECTED_RESULT_FOUR));

    }

    private String getRandomDevice() throws NoDeviceAvailableException {
        TestObjectClient client = new TestObjectRemoteClient("https://app.testobject.com/api/rest", null);
        List<DeviceDescriptor> devices = client.listDevices()
                .stream()
                .filter(device -> device.isAvailable)
                .filter(device -> device.os == DeviceDescriptor.OS.ANDROID)
                .filter(device -> isUIAutomatorCapable(device))
                .collect(Collectors.toList());
        if (devices.size() == 0) {
            throw new NoDeviceAvailableException();
        } else {
            Random random = new Random();
            int index = random.nextInt(devices.size());
            DeviceDescriptor device = devices.get(index);
            return device.id;
        }

    }

    private boolean isUIAutomatorCapable(DeviceDescriptor device) {
        String version = device.osVersion;
        int major = Integer.parseInt(version.substring(0, 1));
        if (major < 4) {
            return false;
        }
        int minor = Integer.parseInt(version.substring(2, 3));
        if (minor < 3) {
            return false;
        }
        return true;
    }

    private class NoDeviceAvailableException extends Throwable {
    }
}