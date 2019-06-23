package com.core;

import com.Constants.Arg;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.*;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AppiumController implements Access {

    private DesiredCapabilities _caps = new DesiredCapabilities();
    private static AppiumDriver _driver = null;
    private Logger logger = LogManager.getLogger(AppiumController.class);
    private String appiumPort = "4723";
    private static BrowserMobProxy server;
    private static String nodeJS = System.getenv("NODE_HOME") + "/node.exe";
    private static String appiumJS = System.getenv("APPIUM_HOME") + "/main.js";
    private static DriverService service;

    private static String serverIp = "127.0.0.1";    //Local
    //    private String serverIp = "172.23.126.97";  //Jenkins

    @Parameters({"device", "apk"})
    @BeforeClass
    public void setup(String device, String apk) throws Exception {
        initDriver(device, apk);
    }

    public AppiumDriver getDriver() {
        return _driver;
    }

    /**
     * Initialize Driver
     * @param device device
     * @param apk apk
     * @throws Exception exception
     */
    private void initDriver(String device, String apk) throws Exception {
        try {
            File appDir = new File("AndroidApp");
            File app = new File(appDir, "***.apk");
            String serverUrl = "http://" + serverIp + ":" + appiumPort + "/wd/hub";
            if (device.equals("NEXUS")) {
                logger.info("Selected device is NEXUS");
                if (apk.equals("Y")) {
                    _caps.setCapability(MobileCapabilityType.APP, app);
                }
                _caps.setCapability(MobileCapabilityType.UDID, NEXUS);
                _caps.setCapability(MobileCapabilityType.DEVICE_NAME, "NEXUS");
                _androidCapabilities(_caps);
                _createService().start();
                logger.info("Argument to driver object : " + serverUrl);
                _driver = new AndroidDriver<>(new URL(serverUrl), _caps);
            } else if (device.equals("PIXEL")) {
                logger.info("Selected device is PIXEL");
                if (apk.equals("Y")) {
                    _caps.setCapability(MobileCapabilityType.APP, app);
                }
                _caps.setCapability(MobileCapabilityType.UDID, PIXEL);
                _caps.setCapability(MobileCapabilityType.DEVICE_NAME, "PIXEL");
                _iosCapabilities(_caps);
                logger.info("Argument to driver object : " + serverUrl);
                _driver = new IOSDriver<>(new URL(serverUrl), _caps);
            } else if (device.equals("WEB")){
                logger.info("Selected device is WEB");
                _caps.setCapability(MobileCapabilityType.UDID, NEXUS);
                _caps.setCapability(MobileCapabilityType.DEVICE_NAME, "NEXUS");
                _createService().start();
                _browserCapabilities(_caps,"chrome");
                logger.info("Argument to driver object : " + serverUrl);
                _driver = new AndroidDriver<>(new URL(serverUrl), _caps);
            }
        } catch (NullPointerException |
                MalformedURLException ex) {
            logger.error("Appium driver could not be initialised for device", ex);
            throw new RuntimeException("Appium driver could not be initialised for device: " + device);
        }
        logger.info("Driver initialized");

    }

    /**
     * Android capabilities
     * @param _caps capabilities
     */
    private void _androidCapabilities(DesiredCapabilities _caps) {
        _caps.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        _caps.setCapability(MobileCapabilityType.NO_RESET, true);
        _caps.setCapability(MobileCapabilityType.FULL_RESET, false);
        _caps.setCapability(MobileCapabilityType.AUTO_WEBVIEW, false);
        _caps.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
        _caps.setCapability(AndroidMobileCapabilityType.APPLICATION_NAME, "UiAutomator2");
        _caps.setCapability(AndroidMobileCapabilityType.ANDROID_INSTALL_TIMEOUT, 60);
        _caps.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.booking");
        _caps.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".startup.HomeActivity");
    }

    /**
     * IOS capabilities
     * @param _caps capabilities
     */
    private void _iosCapabilities(DesiredCapabilities _caps) {
        _caps.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
        _caps.setCapability(MobileCapabilityType.FULL_RESET, false);
        _caps.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
        _caps.setCapability(AndroidMobileCapabilityType.APPLICATION_NAME, "XCUITest");
        _caps.setCapability(MobileCapabilityType.NO_RESET, true);
        _caps.setCapability(IOSMobileCapabilityType.XCODE_ORG_ID, "");
        _caps.setCapability(IOSMobileCapabilityType.XCODE_SIGNING_ID, "");
        _caps.setCapability(IOSMobileCapabilityType.UPDATE_WDA_BUNDLEID, "");
        _caps.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "ios.intent.action.MAIN");
        _caps.setCapability(IOSMobileCapabilityType.APP_NAME, "");
    }

    /**
     * Add Browser capability
     * @param _caps capabilities
     * @param browser browser
     */
    private void _browserCapabilities(DesiredCapabilities _caps, String browser) {
        if (browser.contains("chrome")) {
            _caps.setCapability(AndroidMobileCapabilityType.APPLICATION_NAME, "UiAutomator2");
            _caps.setCapability(MobileCapabilityType.BROWSER_NAME, MobileBrowserType.CHROME);
            _caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "69");
        } else {
            _caps.setCapability(MobileCapabilityType.BROWSER_NAME, MobileBrowserType.SAFARI);
            _caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "8.1");
        }

    }

    /**
     * Add Performance capability
     * @param _caps capability
     */
    private void _performanceCapability(DesiredCapabilities _caps) {
        server = new BrowserMobProxyServer();
        server.setTrustAllServers(true);
        server.start(PROXY_Port);
        server.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
        server.enableHarCaptureTypes(CaptureType.REQUEST_HEADERS, CaptureType.RESPONSE_HEADERS);
        Proxy proxy = ClientUtil.createSeleniumProxy(server);
        _caps.setCapability(MobileCapabilityType.PROXY, proxy);
        server.newHar("appiumPerf.har");
    }

    /**
     * Create appium driver service
     * @return service
     * @throws MalformedURLException url exception
     */
    private static DriverService _createService() throws MalformedURLException {
        service = new AppiumServiceBuilder()
                .usingDriverExecutable(new File(nodeJS))
                .withAppiumJS(new File(appiumJS))
                .withIPAddress(serverIp)
                .usingPort(APPIUM_Port)
                .withArgument(Arg.TIMEOUT, "120")
                .withArgument(Arg.LOG_LEVEL, "warn")
                .build();
        return service;
    }

    /**
     * @return sysDateTime
     */
    private String sysTime() {
        try {
            DateFormat dates = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = new Date();
            return dates.format(date);
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    @AfterClass
    public void tearDown() throws Exception {
        try {
            Har har = server.getHar();
            FileOutputStream fos = new FileOutputStream("C:\\temp\\perf.har");
            har.writeTo(fos);
            server.stop();
        } catch (Exception e) {
            logger.info("Performance test not included");
        }
        _createService().stop();
        _driver.quit();
    }

}
